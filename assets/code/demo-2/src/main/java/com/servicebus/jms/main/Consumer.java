package com.servicebus.jms.main;

import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.JmsMessageListener;
import com.servicebus.jms.utils.JmsMessageListenerWithResponse;
import com.servicebus.jms.utils.Log;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;

public class Consumer {
	public static void main(final String[] args) {
		new Consumer().runConsumer();
	}

	public void runConsumer() {
		long minutesToRunConsumers = 5;
		String topicName = "OrderTopic";
		String procurementConsumerName = "ProcurementConsumer";
		String factoryConsumerName = "FactoryConsumer";
		String warehouseNAConsumerName = "WarehouseNAConsumer";
		String warehouseEUConsumerName = "WarehouseEUConsumer";
		String invoicingConsumerName = "InvoicingConsumer";
		String bulkLoadConsumerName = "BulkLoadConsumer";
		JMSContext jmsContext = null;

		try {
			ConnectionFactory connectionFactory = JmsConnectionFactory.Get();
			jmsContext = connectionFactory.createContext();
			Topic topic = jmsContext.createTopic(topicName);

//			Log.section("1. Receive messages from subscriptions");
//			JMSConsumer jmsProcurementConsumer = jmsContext.createSharedDurableConsumer(topic, procurementConsumerName);
//			jmsProcurementConsumer.setMessageListener(new JmsMessageListener(procurementConsumerName));
//
//			JMSConsumer jmsFactoryConsumer = jmsContext.createSharedDurableConsumer(topic, factoryConsumerName);
//			jmsFactoryConsumer.setMessageListener(new JmsMessageListener(factoryConsumerName));

//			Log.section("2 & 3. Receive messages from filtered subscriptions");
//			JMSConsumer jmsWarehouseNAConsumer = jmsContext.createSharedDurableConsumer(topic, warehouseNAConsumerName, "Region = 'NorthAmerica'");
//			jmsWarehouseNAConsumer.setMessageListener(new JmsMessageListener(warehouseNAConsumerName));
//
//			JMSConsumer jmsWarehouseEUConsumer = jmsContext.createSharedDurableConsumer(topic, warehouseEUConsumerName, "Region = 'Europe'");
//			jmsWarehouseEUConsumer.setMessageListener(new JmsMessageListener(warehouseEUConsumerName));

//			Log.section("4. Receive messages from subscription and send back response");
//			JMSConsumer jmsInvoicingConsumer = jmsContext.createSharedDurableConsumer(topic, invoicingConsumerName, "Pattern = 'RequestResponse'");
//			jmsInvoicingConsumer.setMessageListener(new JmsMessageListenerWithResponse(invoicingConsumerName, jmsContext));

			Log.section("5. Receive large messages");
			JMSConsumer jmsBulkLoadConsumer = jmsContext.createSharedDurableConsumer(topic, bulkLoadConsumerName, "Pattern = 'LargeMessage'");
			jmsBulkLoadConsumer.setMessageListener(new JmsMessageListener(bulkLoadConsumerName));

			Thread.sleep(minutesToRunConsumers * 60 * 1000);
			Log.section("Consumers closing");
//			jmsProcurementConsumer.close();
//			jmsFactoryConsumer.close();
//			jmsWarehouseNAConsumer.close();
//			jmsWarehouseEUConsumer.close();
//			jmsInvoicingConsumer.close();
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