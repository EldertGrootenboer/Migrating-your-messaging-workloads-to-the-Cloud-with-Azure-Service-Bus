package com.servicebus.jms.main;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import com.servicebus.jms.utils.JmsConnectionFactory;
import com.servicebus.jms.utils.JmsTopic;
import com.servicebus.jms.utils.Log;
import com.servicebus.jms.utils.JmsMessageListener;
import com.servicebus.jms.utils.JmsMessageListenerWithResponse;

public class Consumer {
	public static void main(final String[] args) throws Exception {
		new Consumer().runConsumer();
	}

	public void runConsumer() {
		long minutesToRunConsumers = 1;
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
			Topic topic = JmsTopic.Get(connectionFactory, topicName);

			Log.Section("Receive messages from subscriptions");
			JMSConsumer jmsProcurementConsumer = jmsContext.createSharedDurableConsumer(topic, procurementConsumerName);
			jmsProcurementConsumer.setMessageListener(new JmsMessageListener(procurementConsumerName));
			
			JMSConsumer jmsFactoryConsumer = jmsContext.createSharedDurableConsumer(topic, factoryConsumerName);
			jmsFactoryConsumer.setMessageListener(new JmsMessageListener(factoryConsumerName));
			
			
			
			Log.Section("Receive messages from filtered subscriptions");
			JMSConsumer jmsWarehouseNAConsumer = jmsContext.createSharedDurableConsumer(topic, warehouseNAConsumerName, "Region = 'NorthAmerica'");
			jmsWarehouseNAConsumer.setMessageListener(new JmsMessageListener(warehouseNAConsumerName));
			
			JMSConsumer jmsWarehouseEUConsumer = jmsContext.createSharedDurableConsumer(topic, warehouseEUConsumerName, "Region = 'Europe'");
			jmsWarehouseEUConsumer.setMessageListener(new JmsMessageListener(warehouseEUConsumerName));
			
			
			
			Log.Section("Receive messages from subscription and send back response");
			JMSConsumer jmsInvoicingConsumer = jmsContext.createSharedDurableConsumer(topic, invoicingConsumerName, "Pattern = 'RequestResponse'");
			jmsInvoicingConsumer.setMessageListener(new JmsMessageListenerWithResponse(invoicingConsumerName, jmsContext));
			

			
			Log.Section("Receive large messages");
			JMSConsumer jmsBulkLoadConsumer = jmsContext.createSharedDurableConsumer(topic, bulkLoadConsumerName, "Pattern = 'LargeMessage'");
			jmsBulkLoadConsumer.setMessageListener(new JmsMessageListener(bulkLoadConsumerName));
			
			
			
			Thread.sleep(minutesToRunConsumers * 60 * 1000);
			Log.Section("Consumers closing");
			jmsProcurementConsumer.close();
			jmsFactoryConsumer.close();
			jmsWarehouseNAConsumer.close();
			jmsWarehouseEUConsumer.close();
			jmsInvoicingConsumer.close();
			jmsBulkLoadConsumer.close();

		} catch (Exception excep) {
			excep.printStackTrace();
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}


}