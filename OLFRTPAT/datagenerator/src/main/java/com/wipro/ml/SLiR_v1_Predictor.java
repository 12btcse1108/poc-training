package com.wipro.ml;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.StreamingLinearRegressionWithSGD;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.util.RecurringTimer;
import org.apache.spark.util.SystemClock;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.wipro.constants.ConfigConstants;
import com.wipro.db.MLDBHelper;
import com.wipro.kafka.KafkaProducer;
import com.wipro.util.ReadConfig;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import scala.Tuple2;
import scala.runtime.AbstractFunction1;
import scala.runtime.BoxedUnit;

public class SLiR_v1_Predictor {

	private static int NUM_FEATURES;
	private static String predictSink;
	private static String predictSource;

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

	private static Vector getRecordVector(String message) {
		Map<String, Double> map = new HashMap<String, Double>();
		try {
			map = new ObjectMapper().readValue(message, new TypeReference<Map<String, Double>>() {
			});
		} catch (IOException e) {
			System.out.println(e);
		}
		double[] vector = new double[NUM_FEATURES + 1];
		for (int i = 0; i <= NUM_FEATURES; i++) {
			vector[i] = map.get("f" + i);
		}
		return Vectors.dense(vector);
	}

	private static Set<String> getTopicSet(String topic) {
		Set<String> topicSet = new HashSet<String>();
		topicSet.add(topic);
		return topicSet;
	}

	public static void main(String[] args) {
		String logicSetName = args[0];
		new SLiR_v1_Predictor().doTaskTemp(logicSetName);
	}

	StreamingLinearRegressionWithSGD slrModel = null;

	public void doTask(String logicSetName) {
		getParamsFromDB(logicSetName);
		KafkaProducer kp = KafkaProducer.getInstance();
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);
		SparkConf sparkConf = new SparkConf().setAppName("StockTickML_v1_Predictor")
				.set("spark.streaming.receiver.maxRate", "5").setMaster("local[2]");
		// Create the context with 500 milli-seconds batch size
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(500));

		slrModel = new StreamingLinearRegressionWithSGD().setInitialWeights(getInitWeightVect());
		new RecurringTimer(new SystemClock(), 1000, updateModel(), "ModelUpdate").start();
		JavaPairInputDStream<String, String> predictMessages = KafkaUtils.createDirectStream(jssc, String.class,
				String.class, StringDecoder.class, StringDecoder.class, getKafkaParams(), getTopicSet(predictSource));

		JavaPairDStream<Vector, Vector> predictData = predictMessages.map(Tuple2::_2).mapToPair(x -> {
			Vector v = getFeatureVector(x);
			Vector _v = getRecordVector(x);
			return new Tuple2<Vector, Vector>(v, _v);
		});
		predictData.join(slrModel.predictOnValues(predictData.mapToPair(x -> {
			return new Tuple2<Vector, Vector>(x._1, x._1);
		}))).foreachRDD(x -> {
			x.foreach(y -> {
				StringJoiner sj = new StringJoiner(",");
				for (double d : y._2._1.toArray()) {
					sj.add(d + "");
				}
				String message = sj.toString() + ":" + y._2._2;
				kp.execute(predictSink, message);
			});
		});
		jssc.start();
		jssc.awaitTermination();
	}

	public void doTaskTemp(String logicSetName) {
		getParamsFromDB(logicSetName);
		KafkaProducer kp = KafkaProducer.getInstance();

		slrModel = new StreamingLinearRegressionWithSGD().setInitialWeights(getInitWeightVect());
		new RecurringTimer(new SystemClock(), 1000, updateModel(), "ModelUpdate").start();

		Properties properties = new Properties();
		properties.put("zookeeper.connect", "localhost:2181");
		properties.put("group.id", "test-group");
		ConsumerConfig consumerConfig = new ConsumerConfig(properties);

		ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(predictSource, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector
				.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(predictSource).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		while (it.hasNext()) {
			String x = new String(it.next().message());
			Vector v = getFeatureVector(x);
			Vector _v = getRecordVector(x);
			StringJoiner sj = new StringJoiner(",");
			for (double d : _v.toArray()) {
				sj.add(d + "");
			}
			String message = sj.toString() + ":" + slrModel.latestModel().predict(v);
			kp.execute(predictSink, message);
		}
	}

	private void getParamsFromDB(String logicSetName) {
		try {
			MLDBHelper.populateParams(logicSetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		predictSource = MLDBHelper.getLogicSet().getPredictTopicInput();
		predictSink = MLDBHelper.getLogicSet().getPredictTopicOutput();
		NUM_FEATURES = MLDBHelper.getLogicSet().getContinousVariable().split(",").length;
	}

	private AbstractFunction1<Object, BoxedUnit> updateModel() {
		return new AbstractFunction1<Object, BoxedUnit>() {
			@Override
			public BoxedUnit apply(Object arg0) {
				try {
					slrModel = new StreamingLinearRegressionWithSGD().setInitialWeights(getInitWeightVect());
					System.out.println("Loaded updated model with : " + slrModel.latestModel().weights());
				} catch (Exception e) {
					System.out.println("Model updation failed..");
				}
				return null;
			}
		};
	}
}