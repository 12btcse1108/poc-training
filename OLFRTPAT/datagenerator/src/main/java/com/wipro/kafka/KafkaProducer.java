package com.wipro.kafka;

import java.io.Serializable;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer implements Serializable {

	private static Producer<String, String> producer;

	private static final long serialVersionUID = -9053519078378703277L;

	public static KafkaProducer getInstance() {
		KafkaProducer kp = new KafkaProducer();
		return kp;
	}

	private KafkaProducer() {
		init();
	}

	private String castToString(Object obj) {
		String message = null;
		try {
			if (obj instanceof String) {
				message = (String) obj;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return message;
	}

	public void destroy() {
		producer.close();
	}

	public void execute(String topic, Object data) {
		if (producer == null) {
			producer = getInstance().getProducer();
		}
		producer.send(new KeyedMessage<String, String>(topic, castToString(data)));
	}

	public Producer<String, String> getProducer() {
		return producer;
	}

	protected void init() {
		Properties props = new Properties();
		props.put("metadata.broker.list", "am372811-PC:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("partitioner.class", "com.wipro.chatbot.kafka.SimplePartitioner");
		props.put("request.required.acks", "1");

		ProducerConfig config = new ProducerConfig(props);

		producer = new Producer<String, String>(config);
	}

}
