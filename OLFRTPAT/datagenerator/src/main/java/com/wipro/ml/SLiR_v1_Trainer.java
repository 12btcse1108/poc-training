package com.wipro.ml;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.StreamingLinearRegressionWithSGD;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.util.RecurringTimer;
import org.apache.spark.util.SystemClock;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.wipro.constants.ConfigConstants;
import com.wipro.db.MLDBHelper;
import com.wipro.util.ReadConfig;

import kafka.serializer.StringDecoder;
import scala.Tuple2;
import scala.runtime.AbstractFunction1;
import scala.runtime.BoxedUnit;

public class SLiR_v1_Trainer {

	private static double learningRate = 0.000001;
	private static int NUM_FEATURES = 3;
	private static int numIterations = 1000000;
	private static String trainSource = "stockTickTrainTopic1_0";

	private static Vector getFeatureVector(String message) {
		Map<String, Double> map = new HashMap<String, Double>();
		try {
			map = new ObjectMapper().readValue(message, new TypeReference<Map<String, Double>>() {
			});
		} catch (IOException e) {
			System.out.println(e);
		}
		double[] vector = new double[NUM_FEATURES];
		for (int i = 0; i < NUM_FEATURES; i++) {
			vector[i] = map.get("f" + i);
		}
		return Vectors.dense(vector);
	}

	private static Vector getInitWeightVect() {
		try {
			return MLDBHelper.fetchWeights(NUM_FEATURES);
		} catch (Exception e) {
			System.out.println("Some Error occured while fetching Weights" + e.getMessage());
			return Vectors.dense(new double[NUM_FEATURES]);
		}
	}

	private static Map<String, String> getKafkaParams() {
		Map<String, String> kafkaParams = new HashMap<>();
		kafkaParams.put("metadata.broker.list", ReadConfig.getConfig(ConfigConstants.KAFKA_BROKERS));
		kafkaParams.put("zookeeper.connect", ReadConfig.getConfig(ConfigConstants.KAFKA_ZKQUORAM));
		kafkaParams.put("group.id", UUID.randomUUID().toString());
		kafkaParams.put("zookeeper.connection.timeout.ms", "10000");
		kafkaParams.put("auto.offset.reset", "largest");
		kafkaParams.put("enable.auto.commit", "false");
		return kafkaParams;
	}

	private static double getLabel(String message) {
		Map<String, Double> map = new HashMap<String, Double>();
		try {
			map = new ObjectMapper().readValue(message, new TypeReference<Map<String, Double>>() {
			});
		} catch (IOException e) {
		}
		return map.get("f" + NUM_FEATURES);
	}

	private static Set<String> getTopicSet(String topic) {
		Set<String> topicSet = new HashSet<String>();
		topicSet.add(topic);
		return topicSet;
	}

	public static void main(String[] args) {
		String logicSetName = args[0];
		new SLiR_v1_Trainer().doTask(logicSetName);
	}

	StreamingLinearRegressionWithSGD slrModel = null;

	public void doTask(String logicSetName) {
		getParamsFromDB(logicSetName);
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);
		SparkConf sparkConf = new SparkConf().setAppName("StockTickML_v1_Trainer")
				.set("spark.streaming.receiver.maxRate", "5").setMaster("local[2]");
		// Create the context with 500 milli-seconds batch size
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(500));

		slrModel = new StreamingLinearRegressionWithSGD().setInitialWeights(getInitWeightVect())
				.setStepSize(learningRate).setNumIterations(numIterations);
		new RecurringTimer(new SystemClock(), 1000, saveModel(), "ModelSave").start();
		JavaPairInputDStream<String, String> trainMessages = KafkaUtils.createDirectStream(jssc, String.class,
				String.class, StringDecoder.class, StringDecoder.class, getKafkaParams(), getTopicSet(trainSource));
		JavaDStream<LabeledPoint> trainData = trainMessages.map(Tuple2::_2).map(x -> {
			Vector v = getFeatureVector(x);
			return new LabeledPoint(getLabel(x), v);
		});
		trainData.cache();
		slrModel.trainOn(trainData);
		jssc.start();
		jssc.awaitTermination();
	}

	private void getParamsFromDB(String logicSetName) {
		try {
			MLDBHelper.populateParams(logicSetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		trainSource = MLDBHelper.getLogicSet().getTrainTopic();
		learningRate = MLDBHelper.getLogicSet().getLearningRate();
		numIterations = MLDBHelper.getLogicSet().getNumIterations();
		NUM_FEATURES = MLDBHelper.getLogicSet().getContinousVariable().split(",").length;
	}

	private AbstractFunction1<Object, BoxedUnit> saveModel() {
		return new AbstractFunction1<Object, BoxedUnit>() {
			@Override
			public BoxedUnit apply(Object arg0) {
				try {
					MLDBHelper.saveWeights(slrModel.latestModel().weights());
					System.out.println("Saved model with weights : " + slrModel.latestModel().weights());
				} catch (Exception e) {
					System.out.println("Model saving failed..");
				}
				return null;
			}
		};
	}
}