package com.wipro.db;

import java.sql.Connection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import com.wipro.db.dto.LogicSet;
import com.wipro.util.DBConnection;

public class MLDBHelper {

	private static LogicSet logicSet;

	public static void main(String[] args) throws Exception {
		populateParams("StockTick");
		System.out.println(logicSet.toString());
	}

	public static void populateParams(String logicSetName) throws Exception {
		Connection conn = DBConnection.getConnection();

		Map<String, Object> map = DBConnection.readFromDatabase(conn,
				"select logicset_id,msg_name,topicName,algorithmName,"
						+ "trainTopic,predictTopicInput,predictTopicOutput,regularisation,"
						+ "learningRate,modelSaveInterval,algorithm,continousVariable,"
						+ "categoryVariable,predictionVariable,weights,analyzeState "
						+ "from logicset where algorithm='" + logicSetName + "'");
		int id = Integer.parseInt(map.get("logicset_id").toString());
		String msgName = map.get("msg_name").toString();
		String dataSource = map.get("topicName").toString();
		String algorithmName = map.get("algorithmName").toString();
		String trainTopic = map.get("trainTopic").toString();
		String predictTopicInput = map.get("predictTopicInput").toString();
		String predictTopicOutput = map.get("predictTopicOutput").toString();
		int regularization = Integer.parseInt(map.get("numIterations").toString());
		double learningRate = Double.parseDouble(map.get("learningRate").toString());
		int modelSaveInterval = Integer.parseInt(map.get("modelSaveInterval").toString());
		String name = map.get("algorithm").toString();
		String continousVariable = map.get("continousVariable").toString();
		String categoricalVariable = map.get("categoryVariable").toString();
		String predictionVariable = map.get("predictionVariable").toString();
		String weights = map.get("weights").toString();
		String analyzeState = map.get("analyzeState").toString();

		logicSet = new LogicSet(id, msgName, dataSource, algorithmName, trainTopic, predictTopicInput,
				predictTopicOutput, regularization, learningRate, modelSaveInterval, name, continousVariable,
				categoricalVariable, predictionVariable, weights, analyzeState);
		DBConnection.connectionClose(conn);
	}

	public static void saveWeights(Vector v) throws Exception {
		Connection conn = DBConnection.getConnection();
		DBConnection.insertIntoDatabase(conn, StringUtils.join(v.toArray(), ','), logicSet.getName());
		DBConnection.connectionClose(conn);
	}

	public static Vector fetchWeights(int numFeatures) throws Exception {
		double[] vector = new double[numFeatures];
		Connection conn = DBConnection.getConnection();
		try {
			String query = "select weights from logicset where algorithm='" + logicSet.getName() + "'";
			String weights = DBConnection.readWeightsFromDB(conn, query);
			if (!("NULL".equalsIgnoreCase(weights) || weights == null)) {
				String[] array = weights.split(",");
				for (int i = 0; i < numFeatures; i++) {
					vector[i] = Double.parseDouble(array[i]);
				}
			}
			return Vectors.dense(vector);
		} finally {
			DBConnection.connectionClose(conn);
		}
	}

	public static LogicSet getLogicSet() {
		return logicSet;
	}
}
