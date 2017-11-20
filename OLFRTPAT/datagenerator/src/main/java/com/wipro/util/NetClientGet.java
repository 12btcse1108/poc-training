package com.wipro.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NetClientGet {
	private static ObjectMapper mapper = new ObjectMapper();

	public static Map<String, String> call(String restEndPoint) {
		StringBuilder response = new StringBuilder();
		try {
			URL url = new URL(restEndPoint);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				response.append(output);
			}
			conn.disconnect();
			return mapper.readValue(response.substring(4).substring(0, response.length() - 5),
					new TypeReference<Map<String, String>>() {
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}