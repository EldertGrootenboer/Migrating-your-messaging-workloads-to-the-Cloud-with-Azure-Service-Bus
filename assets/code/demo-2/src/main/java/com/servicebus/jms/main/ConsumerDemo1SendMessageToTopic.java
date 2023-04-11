package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.JmsMessageListener;
import com.servicebus.jms.utils.Log;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;

public class ConsumerDemo1SendMessageToTopic {
	public static void main(final String[] args) {
		new ConsumerDemo1SendMessageToTopic().runConsumer();
	}
 
	public void runConsumer() {
		long minutesToRunConsumers = 5;
		String topicName = "OrderTopic";
		String factoryConsumerName = "FactoryConsumer";
		JMSContext jmsContext = null;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);

			Log.section("1. Receive messages from subscriptions");
			JMSConsumer jmsFactoryConsumer = jmsContext.createSharedDurableConsumer(topic, factoryConsumerName);
			jmsFactoryConsumer.setMessageListener(new JmsMessageListener(factoryConsumerName));

			Thread.sleep(minutesToRunConsumers * 60 * 1000);
			Log.section("Consumers closing");
			jmsFactoryConsumer.close();
		} catch (Exception e) {
			throw new RuntimeException("Consumer could not be run. " + e);
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}