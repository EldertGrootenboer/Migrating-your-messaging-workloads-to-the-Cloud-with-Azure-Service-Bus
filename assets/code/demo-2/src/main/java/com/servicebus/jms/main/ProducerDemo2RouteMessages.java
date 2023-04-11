package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.Log;

import javax.jms.*;

public class ProducerDemo2RouteMessages {
	public static void main(final String[] args) {
		new ProducerDemo2RouteMessages().runProducer();
	}

	public void runProducer() {
		String topicName = "OrderTopic";
		JMSContext jmsContext = null;
		JMSProducer jmsProducer;
		Message message;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);
			jmsProducer = jmsContext.createProducer();

			Log.section("2. Send messages for warehouses");
			message = jmsContext.createTextMessage("Order to be delivered by NA warehouse");
			message.setStringProperty("Region", "NorthAmerica");
			jmsProducer.send(topic, message);
			Log.sentMessage(topic.getTopicName(), message);

			message = jmsContext.createTextMessage("Order to be delivered by EU warehouse");
			message.setStringProperty("Region", "Europe");
			jmsProducer.send(topic, message);
			Log.sentMessage(topic.getTopicName(), message);

		} catch (Exception e) {
			throw new RuntimeException("Error occurred running producer. " + e);
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}