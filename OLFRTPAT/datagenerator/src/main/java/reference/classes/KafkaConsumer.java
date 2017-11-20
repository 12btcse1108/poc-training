package reference.classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

public class KafkaConsumer {

	private static String topic = "stockTickTrainTopic";

	private static Map<String, String> getKafkaParams() {
		Map<String, String> kafkaParams = new HashMap<>();
		kafkaParams.put("metadata.broker.list", "am372811-PC:9092");
		kafkaParams.put("zookeeper.connect", "am372811-PC:2181");
		kafkaParams.put("group.id", UUID.randomUUID().toString());
		kafkaParams.put("zookeeper.connection.timeout.ms", "10000");
		kafkaParams.put("kafka.auto.offset.reset", "smallest");
		kafkaParams.put("enable.auto.commit", "false");
		return kafkaParams;
	}

	private static Map<String, Integer> getTopicMap(String topic) {
		Map<String, Integer> topicMap = new HashMap<>();
		topicMap.put(topic, 1);
		return topicMap;
	}

	private static Set<String> getTopicSet(String topic) {
		Set<String> topicSet = new HashSet<String>();
		topicSet.add(topic);
		return topicSet;
	}

	private static void kafkaConsumer() {
		SparkConf sparkConf = new SparkConf().setAppName("JavaKafkaTopicRead").setMaster("local[2]");
		// Create the context with 2 seconds batch size
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(10000));

		JavaPairInputDStream<String, String> messages = KafkaUtils.createStream(jssc, "am372811-PC:2181",
				UUID.randomUUID().toString(), getTopicMap(topic));
		JavaDStream<String> lines = messages.map(Tuple2::_2);

		JavaDStream<String> words = lines.map(x -> x + 12);

		System.out.println("Printing...");
		words.foreachRDD(x -> {
			x.foreach(y -> {
				System.out.println(y);
			});
		});
		words.print();
		jssc.start();
		jssc.awaitTermination();
	}

	private static void kafkaDirectConsumer() {
		SparkConf sparkConf = new SparkConf().setAppName("JavaKafkaTopicRead").setMaster("local[2]");
		// Create the context with 2 seconds batch size
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(10000));

		JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(jssc, String.class, String.class,
				StringDecoder.class, StringDecoder.class, getKafkaParams(), getTopicSet(topic));

		JavaDStream<String> lines = messages.map(Tuple2::_2);

		JavaDStream<String> words = lines.map(x -> x + 12);
		System.out.println("Direct Printing...");
		words.foreachRDD(x -> {
			x.foreach(y -> {
				System.out.println(y);
			});
		});
		words.print();
		jssc.start();
		jssc.awaitTermination();
	}

	/*
	 * static Map map =(Map)ReadConfig.getConfig().get("kafka"); private static
	 * String brokers = (String) map.get("brokers"); private static String
	 * zkQuoram =(String) map.get("zkQuoram") ;
	 */
	public static void main(String[] args) throws Exception {
		String consumerType = "DIRECT";

		switch (consumerType) {
		case "DIRECT":
			kafkaDirectConsumer();
			break;
		default:
			kafkaConsumer();
			break;
		}

	}
}
