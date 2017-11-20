package reference.classes;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.wipro.constants.ConfigConstants;
import com.wipro.db.MLDBHelper;
import com.wipro.util.ReadConfig;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

public class DataGenStockTick {

	private static String predictSourceTopic = "stockTickTrainTopic2_0";
	private static String sourceTopic = "stockTickTrainTopic0_0";
	private static String trainSourceTopic = "stockTickTrainTopic1_0";

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

	private static void getParamsFromDB(String logicSetName) {
		try {
			MLDBHelper.populateParams(logicSetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sourceTopic = MLDBHelper.getLogicSet().getDataSource();
		trainSourceTopic = MLDBHelper.getLogicSet().getTrainTopic();
		predictSourceTopic = MLDBHelper.getLogicSet().getPredictTopicInput();
	}

	private static Set<String> getTopicSet(String topic) {
		Set<String> topicSet = new HashSet<String>();
		topicSet.add(topic);
		return topicSet;
	}

	public static void main(String[] args) {
		getParamsFromDB(args[0]);
		KafkaProducer kp = KafkaProducer.getInstance();
		SparkConf sparkConf = new SparkConf().setAppName("DataGenStockTick").setMaster("local[2]");
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(1000));
		JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(jssc, String.class, String.class,
				StringDecoder.class, StringDecoder.class, getKafkaParams(), getTopicSet(sourceTopic));

		JavaDStream<String> records = messages.map(Tuple2::_2);

		JavaDStream<Vector> featureVector = records.map(new Function<String, Vector>() {
			private static final long serialVersionUID = 272415769961008316L;

			@Override
			public Vector call(String record) throws Exception {
				List<Double> lst = Arrays.stream(record.split(",")).map((x) -> Double.parseDouble(x))
						.collect(Collectors.toList());
				Double[] arr = lst.toArray(new Double[lst.size()]);
				return Vectors.dense(ArrayUtils.toPrimitive(arr));
			}
		});
		featureVector.foreachRDD(x -> {
			x.foreach(y -> {
				System.out.println("Data is :" + y);
				String message = toJsonString(y);
				kp.execute(trainSourceTopic, message);
				kp.execute(predictSourceTopic, message);
			});
		});
		jssc.start();
		jssc.awaitTermination();
	}

	public static String toJsonString(Vector features) {
		Map<String, Double> featureMap = new HashMap<>();
		int i = 0;
		for (Double feature : features.toArray()) {
			featureMap.put("f" + i++, feature);
		}
		String json = "{}";
		try {
			json = new ObjectMapper().writeValueAsString(featureMap);
		} catch (IOException e) {
		}
		return json;
	}
}
