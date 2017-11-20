package com.wipro.db.dto;

public class LogicSet {

	private int id;
	private String msgName;
	private String dataSource;
	private String algorithmName;
	private String trainTopic;
	private String predictTopicInput;
	private String predictTopicOutput;
	private int numIterations;
	private double learningRate;;
	private int modelSaveInterval;
	private String name;
	private String continousVariable;
	private String categoricalVariable;
	private String predictionVariable;
	private String weights;
	private String analyzeState;

	public LogicSet(int id, String msgName, String dataSource, String algorithmName, String trainTopic,
			String predictTopicInput, String predictTopicOutput, int numIterations, double learningRate,
			int modelSaveInterval, String name, String continousVariable, String categoricalVariable,
			String predictionVariable, String weights, String analyzeState) {
		this.id = id;
		this.msgName = msgName;
		this.dataSource = dataSource;
		this.algorithmName = algorithmName;
		this.trainTopic = trainTopic;
		this.predictTopicInput = predictTopicInput;
		this.predictTopicOutput = predictTopicOutput;
		this.numIterations = numIterations;
		this.learningRate = learningRate;
		this.modelSaveInterval = modelSaveInterval;
		this.name = name;
		this.continousVariable = continousVariable;
		this.categoricalVariable = categoricalVariable;
		this.predictionVariable = predictionVariable;
		this.weights = weights;
		this.analyzeState = analyzeState;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsgName() {
		return msgName;
	}

	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getTrainTopic() {
		return trainTopic;
	}

	public void setTrainTopic(String trainTopic) {
		this.trainTopic = trainTopic;
	}

	public String getPredictTopicInput() {
		return predictTopicInput;
	}

	public void setPredictTopicInput(String predictTopicInput) {
		this.predictTopicInput = predictTopicInput;
	}

	public String getPredictTopicOutput() {
		return predictTopicOutput;
	}

	public void setPredictTopicOutput(String predictTopicOutput) {
		this.predictTopicOutput = predictTopicOutput;
	}

	public int getNumIterations() {
		return numIterations;
	}

	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public int getModelSaveInterval() {
		return modelSaveInterval;
	}

	public void setModelSaveInterval(int modelSaveInterval) {
		this.modelSaveInterval = modelSaveInterval;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContinousVariable() {
		return continousVariable;
	}

	public void setContinousVariable(String continousVariable) {
		this.continousVariable = continousVariable;
	}

	public String getCategoricalVariable() {
		return categoricalVariable;
	}

	public void setCategoricalVariable(String categoricalVariable) {
		this.categoricalVariable = categoricalVariable;
	}

	public String getPredictionVariable() {
		return predictionVariable;
	}

	public void setPredictionVariable(String predictionVariable) {
		this.predictionVariable = predictionVariable;
	}

	public String getWeights() {
		return weights;
	}

	public void setWeights(String weights) {
		this.weights = weights;
	}

	public String getAnalyzeState() {
		return analyzeState;
	}

	public void setAnalyzeState(String analyzeState) {
		this.analyzeState = analyzeState;
	}

	@Override
	public String toString() {
		return "id : " + id + ", msgName : " + msgName + ", dataSource : " + dataSource + ", algorithmName : "
				+ algorithmName + ", trainTopic : " + trainTopic + ", predictTopicInput : " + predictTopicInput
				+ ", predictTopicOutput : " + predictTopicOutput + ", regularization : " + numIterations
				+ ", learningRate : " + learningRate + ", modelSaveInterval : " + modelSaveInterval + ", name : " + name
				+ ", continousVariable : " + continousVariable + ", categoricalVariable : " + categoricalVariable
				+ ", predictionVariable : " + predictionVariable + ", weights : " + weights + ", analyzeState : "
				+ analyzeState;
	}
}
