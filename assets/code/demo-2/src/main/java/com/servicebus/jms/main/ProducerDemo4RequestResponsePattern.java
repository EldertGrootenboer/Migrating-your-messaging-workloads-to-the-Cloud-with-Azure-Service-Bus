package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.Log;

import javax.jms.*;

public class ProducerDemo4RequestResponsePattern {
	public static void main(final String[] args) {
		new ProducerDemo4RequestResponsePattern().runProducer();
	}

	public void runProducer() {
		String topicName = "OrderTopic";
		long deliveryDelayInSeconds = 10;
		JMSContext jmsContext = null;
		JMSProducer jmsProducer;
		Message message;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);
			jmsProducer = jmsContext.createProducer();

			Log.section("4. Send message and wait for response");
			Queue temporaryQueue = jmsContext.createTemporaryQueue();
			message = jmsContext.createTextMessage("Please acknowledge receipt of this message.");
			message.setStringProperty("Pattern", "RequestResponse");
			jmsProducer.setDeliveryDelay(deliveryDelayInSeconds * 1000);
			jmsProducer.setJMSReplyTo(temporaryQueue);
			jmsProducer.send(topic, message);
			Log.sentMessage(topic.getTopicName(), message);

			Log.section("Wait for response message");
			JMSConsumer responseConsumer = jmsContext.createConsumer(temporaryQueue);
			Message response = responseConsumer.receive();
			Log.receivedMessage(temporaryQueue.getQueueName(), response);
			response.acknowledge();

		} catch (Exception e) {
			throw new RuntimeException("Error occurred running producer. " + e);
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}