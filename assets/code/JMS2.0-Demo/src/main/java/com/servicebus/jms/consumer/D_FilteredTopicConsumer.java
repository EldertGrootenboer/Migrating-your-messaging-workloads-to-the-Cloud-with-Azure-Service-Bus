package com.servicebus.jms.consumer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;

import com.servicebus.jms.utils.Log;

public class D_FilteredTopicConsumer implements Runnable {
	private Topic topic;
	private ConnectionFactory connectionFactory;

	public D_FilteredTopicConsumer(Topic topic, ConnectionFactory connectionFactory) {
		this.topic = topic;
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;
		String consumer1Name = "D_SharedDurableFilteredConsumer1";
		String consumer2Name = "D_SharedDurableFilteredConsumer2";

		try {
			jmsContext = connectionFactory.createContext();
			JMSConsumer jmsConsumer1 = jmsContext.createSharedDurableConsumer(topic, consumer1Name, "Region = 'NorthAmerica'");			
			JMSConsumer jmsConsumer2 = jmsContext.createSharedDurableConsumer(topic, consumer2Name, "Region = 'Europe'");

			Message message = jmsConsumer1.receive(2000);
			Log.ReceivedMessage(consumer1Name, message);
			message.acknowledge();

			message = jmsConsumer2.receive(2000);
			Log.ReceivedMessage(consumer2Name, message);
			message.acknowledge();
		} catch (Exception excep) {
			excep.printStackTrace();
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
}