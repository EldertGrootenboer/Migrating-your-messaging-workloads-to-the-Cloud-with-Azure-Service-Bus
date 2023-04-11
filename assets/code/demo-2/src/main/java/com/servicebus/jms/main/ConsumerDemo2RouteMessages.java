package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.JmsMessageListener;
import com.servicebus.jms.utils.Log;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;

public class ConsumerDemo2RouteMessages {
	public static void main(final String[] args) {
		new ConsumerDemo2RouteMessages().runConsumer();
	}

	public void runConsumer() {
		long minutesToRunConsumers = 5;
		String topicName = "OrderTopic";
		String warehouseNAConsumerName = "WarehouseNAConsumer";
		String warehouseEUConsumerName = "WarehouseEUConsumer";
		JMSContext jmsContext = null;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);

			Log.section("2. Receive messages from filtered subscriptions");
			JMSConsumer jmsWarehouseNAConsumer = jmsContext.createSharedDurableConsumer(topic, warehouseNAConsumerName, "Region = 'NorthAmerica'");
			jmsWarehouseNAConsumer.setMessageListener(new JmsMessageListener(warehouseNAConsumerName));

			JMSConsumer jmsWarehouseEUConsumer = jmsContext.createSharedDurableConsumer(topic, warehouseEUConsumerName, "Region = 'Europe'");
			jmsWarehouseEUConsumer.setMessageListener(new JmsMessageListener(warehouseEUConsumerName));

			Thread.sleep(minutesToRunConsumers * 60 * 1000);
			Log.section("Consumers closing");
			jmsWarehouseNAConsumer.close();
			jmsWarehouseEUConsumer.close();
		} catch (Exception e) {
			throw new RuntimeException("Consumer could not be run. " + e);
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}