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

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.wipro.constants.ConfigConstants;
import com.wipro.util.ReadConfig;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

public class DataGen {

	private static String sinkTopic = "lrTopic3";
	private static String sourceTopic = "lrTopic2";

	private static Map<String, String> getKafkaParams() {
		Map<String, String> kafkaParams = new HashMap<>();
		kafkaParams.put("metadata.broker.list", ReadConfig.getConfig(ConfigConstants.KAFKA_BROKERS));
		kafkaParams.put("zookeeper.connect", ReadConfig.getConfig(ConfigConstants.KAFKA_ZKQUORAM));
		kafkaParams.put("group.id", UUID.randomUUID().toString());
		kafkaParams.put("zookeeper.connection.timeout.ms", "10000");
		// kafkaParams.put("auto.offset.reset", "smallest");
		kafkaParams.put("enable.auto.commit", "false");
		return kafkaParams;
	}

	private static Set<String> getTopicSet(String topic) {
		Set<String> topicSet = new HashSet<String>();
		topicSet.add(topic);
		return topicSet;
	}

	public static void main(String[] args) {
		KafkaProducer kp = KafkaProducer.getInstance();
		SparkConf sparkConf = new SparkConf().setAppName("DataGen");
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(1000));
		JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(jssc, String.class, String.class,
				StringDecoder.class, StringDecoder.class, getKafkaParams(), getTopicSet(sourceTopic));

		JavaDStream<String> records = messages.map(Tuple2::_2);

		records.map(new Function<String, List<Double>>() {
			private static final long serialVersionUID = 272415769961008316L;

			@Override
			public List<Double> call(String record) throws Exception {
				return Arrays.stream(record.split(",")).map((x) -> Double.parseDouble(x)).collect(Collectors.toList());
			}
		}).foreachRDD(x -> {
			x.map(y -> {
				String message = toJsonString(y);
				kp.execute(sinkTopic, message);
				return message;
			}).count();
		});
		jssc.start();
		jssc.awaitTermination();
	}

	public static String toJsonString(List<Double> features) {
		Map<String, Double> featureMap = new HashMap<>();
		int i = 0;
		for (Double feature : features) {
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
