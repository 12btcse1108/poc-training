package reference.classes;

import java.util.StringJoiner;

import com.wipro.kafka.KafkaProducer;

public class LinearRegressionDatasetGen {

	private final static int NUM_FEATURES = 10;
	private final static int RATE_CONTROLLER = 5;
	private static int TEST_DATA_COUNT = 10;
	private final static String testTopic = "lrTopic2";
	private static int TRAIN_DATA_COUNT = 100;
	private final static String trainTopic = "lrTopic1";

	private static double[] WEIGHTS = new double[NUM_FEATURES];
	static {
		for (int i = 0; i < NUM_FEATURES; i++) {
			WEIGHTS[i] = (i);
		}
	}

	private static void generateData(int dataCount, String topic) {
		KafkaProducer kp = KafkaProducer.getInstance();
		for (int i = 0; i < NUM_FEATURES; i++) {
			System.out.print(WEIGHTS[i] + ",");
		}
		double x[] = new double[NUM_FEATURES];

		double train_dataset[][] = new double[dataCount][NUM_FEATURES + 1];

		System.out.println("Publishing data to kafka topic : " + topic + "...");
		for (int n = 0, i; n < dataCount; n++) {
			train_dataset[n] = new double[NUM_FEATURES + 1];
			for (i = 0; i < NUM_FEATURES; i++) {
				double temp = Math.random() % 100;
				x[i] = temp;
				train_dataset[n][i] = temp;
			}
			train_dataset[n][i] = multiplyAndAdd(WEIGHTS, x);
			StringJoiner dataToPublish = new StringJoiner(",");
			for (int j = 0; j <= NUM_FEATURES; j++) {
				dataToPublish.add(train_dataset[n][j] + "");
			}
			kp.execute(topic, dataToPublish.toString());

			if (n % 1000 == 0) {
				System.out.println((n * 100.0) / dataCount + "%");
				sleep(RATE_CONTROLLER);
			}
		}
		System.out.println("100%\ndone..");
	}

	public static void main(String[] args) {
		if (args.length == 2) {
			TRAIN_DATA_COUNT = Integer.parseInt(args[0]);
			TEST_DATA_COUNT = Integer.parseInt(args[1]);
		}
		generateData(TRAIN_DATA_COUNT, trainTopic);
		generateData(TEST_DATA_COUNT, testTopic);
	}

	private static double multiplyAndAdd(double[] weights, double[] x) {
		double y = 0;
		for (int i = 0; i < NUM_FEATURES; i++) {
			y += weights[i] * x[i];
		}
		return y;
	}

	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
}