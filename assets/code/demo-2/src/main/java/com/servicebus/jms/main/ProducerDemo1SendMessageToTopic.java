package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.Log;

import javax.jms.*;

public class ProducerDemo1SendMessageToTopic {
	public static void main(final String[] args) {
		new ProducerDemo1SendMessageToTopic().runProducer();
	}

	public void runProducer() {
		String conference = "JavaOne";
		String topicName = "OrderTopic";
		JMSContext jmsContext = null;
		JMSProducer jmsProducer;
		Message message;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);
			jmsProducer = jmsContext.createProducer();

			Log.section("1. Send general message to topic");
			message = jmsContext.createTextMessage(String.format("Hello %s!", conference));
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