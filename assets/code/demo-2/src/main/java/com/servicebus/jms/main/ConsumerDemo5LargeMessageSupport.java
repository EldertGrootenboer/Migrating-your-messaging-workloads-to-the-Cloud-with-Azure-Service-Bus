package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.JmsMessageListener;
import com.servicebus.jms.utils.Log;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;

public class ConsumerDemo5LargeMessageSupport {
	public static void main(final String[] args) {
		new ConsumerDemo5LargeMessageSupport().runConsumer();
	}

	public void runConsumer() {
		long minutesToRunConsumers = 5;
		String topicName = "OrderTopic";
		String bulkLoadConsumerName = "BulkLoadConsumer";
		JMSContext jmsContext = null;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);

			Log.section("5. Receive large messages");
			JMSConsumer jmsBulkLoadConsumer = jmsContext.createSharedDurableConsumer(topic, bulkLoadConsumerName, "Pattern = 'LargeMessage'");
			jmsBulkLoadConsumer.setMessageListener(new JmsMessageListener(bulkLoadConsumerName));

			Thread.sleep(minutesToRunConsumers * 60 * 1000);
			Log.section("Consumers closing");
			jmsBulkLoadConsumer.close();
		} catch (Exception e) {
			throw new RuntimeException("Consumer could not be run. " + e);
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}