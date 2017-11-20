package com.wipro.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.wipro.kafka.KafkaProducer;

public class CsvToLines {

	public static void main(String[] args) throws InterruptedException {

		String csvFile = "/home/am372811/gitprojects/onlinetraining/datagenerator/src/main/resources/wipro_stock_price.dat";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader(csvFile));
			int i = 0;
			KafkaProducer kp = KafkaProducer.getInstance();
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] country = line.split(cvsSplitBy);
				String dataToPublish = i + "," + country[3].trim() + "," + country[4].trim() + "," + country[5].trim()
						+ "," + country[7].trim().substring(0, country[7].trim().length() - 2) + ","
						+ country[2].trim();
				i++;
				System.out.println("Publishing " + dataToPublish);
				kp.execute("stockTickTrainTopic0_1", dataToPublish);
				Thread.sleep(1000);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
