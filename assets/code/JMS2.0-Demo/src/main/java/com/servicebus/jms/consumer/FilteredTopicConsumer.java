package com.servicebus.jms.consumer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;

import com.servicebus.jms.utils.Log;

public class FilteredTopicConsumer implements Runnable {
	private Topic topic;
	private ConnectionFactory connectionFactory;

	public FilteredTopicConsumer(Topic topic, ConnectionFactory connectionFactory) {
		this.topic = topic;
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;

		try {
			jmsContext = connectionFactory.createContext();
			JMSConsumer jmsConsumer1 = jmsContext.createSharedDurableConsumer(topic, "SharedDurableFilteredConsumer1", "Region = 'NorthAmerica'");			
			JMSConsumer jmsConsumer2 = jmsContext.createSharedDurableConsumer(topic, "SharedDurableFilteredConsumer2", "Region = 'Europe'");

			Message message = jmsConsumer1.receive(2000);
			Log.ReceivedMessage("SharedDurableFilteredConsumer1", message);
			message.acknowledge();

			message = jmsConsumer2.receive(2000);
			Log.ReceivedMessage("SharedDurableFilteredConsumer2", message);
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