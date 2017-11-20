package com.wipro.util.dataset;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.wipro.db.MLDBHelper;
import com.wipro.kafka.KafkaProducer;
import com.wipro.util.NetClientGet;

public class StockTickerLorDG {

	static String code = "G";

	private static String current_change_percent;

	private static String current_close;
	private static String current_trade_price;
	private static Boolean flag = Boolean.TRUE;
	private static String previous_change_percent;
	private static String previous_close;
	private static String previous_trade_price;

	private static final int RATE_CONTROLLER = 1000;

	private static void generateData(String restEndPoint, String topic) {
		try {
			KafkaProducer kp = KafkaProducer.getInstance();
			Map<String, String> map = NetClientGet.call(restEndPoint);

			current_close = map.get("pcls_fix");
			current_trade_price = map.get("l_fix");
			current_change_percent = map.get("cp");

			if (flag == Boolean.TRUE) {
				previous_close = current_close;
				previous_trade_price = current_trade_price;
				previous_change_percent = current_change_percent;
				flag = Boolean.FALSE;
			}
			String label = getLabel(previous_trade_price, current_trade_price);
			String dataToPublish = null;
			switch (code) {
			case "A":
				dataToPublish = previous_close + "," + label;
				break;
			case "B":
				dataToPublish = previous_trade_price + "," + label;
				break;
			case "C":
				dataToPublish = previous_change_percent + "," + label;
				break;
			case "D":
				dataToPublish = previous_close + "," + previous_trade_price + "," + label;
				break;
			case "E":
				dataToPublish = previous_close + "," + previous_change_percent + "," + label;
				break;
			case "F":
				dataToPublish = previous_trade_price + "," + previous_change_percent + "," + label;
				break;
			case "G":
				dataToPublish = previous_close + "," + previous_trade_price + "," + previous_change_percent + ","
						+ label;
				break;
			}
			System.out.println("Publishing " + dataToPublish);
			kp.execute(topic, dataToPublish);
			previous_close = current_close;
			previous_trade_price = current_trade_price;
			previous_change_percent = current_change_percent;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		sleep(RATE_CONTROLLER);
		generateData(restEndPoint, topic);
	}

	private static String getLabel(String previous_trade_price, String current_trade_price) {
		Double ptp = Double.parseDouble(previous_trade_price);
		Double ctp = Double.parseDouble(current_trade_price);
		if (ctp < ptp) {
			return "0";
		} else {
			return "1";
		}
	}

	private static void getParamsFromDB(String logicSetName) {
		try {
			MLDBHelper.populateParams(logicSetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String continousVariables = MLDBHelper.getLogicSet().getContinousVariable();
		String predictionVariable = MLDBHelper.getLogicSet().getPredictionVariable();

		System.out.println(continousVariables);
		System.out.println(predictionVariable);

		junkMethod(continousVariables);
	}

	// This method is written in hurry to add some logic
	private static void junkMethod(String continousVariables) {
		List<String> a = Arrays.asList(continousVariables.split(","));
		if (a.size() == 1) {
			if (a.contains("previous_close")) {
				code = "A";
			} else if (a.contains("previous_trade_price")) {
				code = "B";
			} else if (a.contains("previous_change_percent")) {
				code = "C";
			}
		} else if (a.size() == 2) {
			if (a.contains("previous_close") && a.contains("previous_trade_price")) {
				code = "D";
			}
			if (a.contains("previous_close") && a.contains("previous_change_percent")) {
				code = "E";
			} else if (a.contains("previous_trade_price") && a.contains("previous_change_percent")) {
				code = "F";
			}
		} else {
			code = "G";
		}
	}

	public static void main(String[] args) {
		if (args[0].equalsIgnoreCase("--use_proxy=true")) {
			final String authUser = "am372811";
			final String authPassword = "July@2017788";
			final String proxy_host = "proxy4.wipro.com";
			final String proxy_port = "8080";
			Authenticator.setDefault(new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(authUser, authPassword.toCharArray());
				}
			});
			System.setProperty("http.proxyUser", authUser);
			System.setProperty("http.proxyPassword", authPassword);
			System.getProperties().put("http.proxyHost", proxy_host);
			System.getProperties().put("http.proxyPort", proxy_port);
		}
		String restEndPoint = "http://finance.google.com/finance/info?client=ig&q=BOM:WIPRO";
		String TRAIN_TOPIC = "stockTickTrainTopic0_0";
		getParamsFromDB(args[1]);
		generateData(restEndPoint, TRAIN_TOPIC);
	}

	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
}
