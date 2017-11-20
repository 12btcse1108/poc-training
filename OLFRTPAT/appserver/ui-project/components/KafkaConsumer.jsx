import React from "react";
import { Button } from "react-bootstrap";
import kafka from "kafka";
import net from "net";

class KafkaConsumer extends React.Component {
	componentDidMount() {
		var consumer = new kafka.Consumer({
			// these are the default values
			host: "am372811-PC",
			port: 9092,
			pollInterval: 2000,
			maxSize: 1048576 // 1MB
		});
		/*consumer.on('message', function(topic, message) { 
	    console.log(message)
	})
	consumer.connect(function() {
	    consumer.subscribeTopic({name: 'stockTickTrainTopic3_0', partition: 0})
	})*/
	}

	componentWillUnmount() {
		// Unsubscribe from the consume here.
	}
	render() {
		return (
			<div>
				<span>Hello Amit Kumar!</span>
			</div>
		);
	}
}
export default KafkaConsumer;
