package reference.classes;

import java.util.StringJoiner;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.wipro.kafka.KafkaProducer;

@Deprecated
public class LRDatasetGenSPMOHST {

	private static final Pattern COMMA = Pattern.compile(",");

	private static int counter = 0;
	static int end = 245;

	private final static int NUM_FEATURES = 1;

	static int start = 100;

	private final static String testTopic = "lrTopic2";

	private final static String trainTopic = "lrTopic1";

	private static void generateData(String topic) {
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);
		KafkaProducer kp = KafkaProducer.getInstance();
		SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount").setMaster("local[1]");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		JavaRDD<String> lines = ctx.textFile("/home/am372811/data/dataset/LR/snp/sp500hst.txt", 1);

		lines.foreach(x -> {
			double[] train_dataset = new double[NUM_FEATURES + 1];
			String arr[] = COMMA.split(x);
			train_dataset[0] = Double.parseDouble(arr[2]);
			train_dataset[1] = Double.parseDouble(arr[5]);
			StringJoiner dataToPublish = new StringJoiner(",");
			if (counter > start && counter < end) {
				for (int j = 0; j <= NUM_FEATURES; j++) {
					dataToPublish.add(train_dataset[j] + "");
				}
				kp.execute(trainTopic, dataToPublish.toString());
			}
			counter++;
		});
	}
	public static void main(String[] args) {
		generateData(trainTopic);
	}
}