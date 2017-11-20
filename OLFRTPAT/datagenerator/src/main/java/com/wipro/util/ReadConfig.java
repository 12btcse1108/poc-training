package com.wipro.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.wipro.constants.ConfigConstants;

public final class ReadConfig {

	private static Map<String, Object> CONFIG = getConfig();
	private static String configFileName = "chatbot.yml";

	@SuppressWarnings("unchecked")
	private static Map<String, Object> getConfig() {
		Map<String, Object> config = new HashMap<>();
		YamlReader reader;
		try {
			/*
			 * reader = new YamlReader( new
			 * FileReader(ReadConfig.class.getClassLoader().getResource(
			 * configFileName).getFile()));
			 */
			reader = new YamlReader(new FileReader(
					"/home/am372811/gitprojects/onlinetraining/datagenerator/src/main/resources/chatbot.yml"));
			config = (Map<String, Object>) reader.read();
		} catch (FileNotFoundException | YamlException e) {
			System.err.println("ERROR : Config not read; Abort!!!" + e.getMessage());
		}
		return config;
	}

	@SuppressWarnings("unchecked")
	public static String getConfig(String key) {
		String[] keys = key.split("\\.");
		Map<String, Object> mp = CONFIG;
		for (String k : keys) {
			if (mp.get(k) != null) {
				if (mp.get(k) instanceof Map) {
					mp = (Map<String, Object>) mp.get(k);
				} else
					return (String) mp.get(k);
			}
		}
		return "INVALID KEY";
	}

	public static void main(String[] args) {
		System.out.println(getConfig(ConfigConstants.KAFKA_ZKQUORAM));
	}
}
