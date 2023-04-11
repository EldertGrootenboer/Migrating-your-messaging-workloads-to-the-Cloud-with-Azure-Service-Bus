package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.JmsMessageListenerWithResponse;
import com.servicebus.jms.utils.Log;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;

public class ConsumerDemo4RequestResponsePattern {
	public static void main(final String[] args) {
		new ConsumerDemo4RequestResponsePattern().runConsumer();
	}

	public void runConsumer() {
		long minutesToRunConsumers = 5;
		String topicName = "OrderTopic";
		String invoicingConsumerName = "InvoicingConsumer";
		JMSContext jmsContext = null;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);

			Log.section("4. Receive messages from subscription and send back response");
			JMSConsumer jmsInvoicingConsumer = jmsContext.createSharedDurableConsumer(topic, invoicingConsumerName, "Pattern = 'RequestResponse'");
			jmsInvoicingConsumer.setMessageListener(new JmsMessageListenerWithResponse(invoicingConsumerName, jmsContext));

			Thread.sleep(minutesToRunConsumers * 60 * 1000);
			Log.section("Consumers closing");
			jmsInvoicingConsumer.close();
		} catch (Exception e) {
			throw new RuntimeException("Consumer could not be run. " + e);
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}