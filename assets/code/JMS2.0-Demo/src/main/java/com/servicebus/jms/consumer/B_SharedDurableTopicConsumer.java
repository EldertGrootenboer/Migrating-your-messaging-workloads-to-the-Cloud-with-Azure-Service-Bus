package com.servicebus.jms.consumer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;

import com.servicebus.jms.utils.Log;

public class B_SharedDurableTopicConsumer implements Runnable {
	private Topic topic;
	private ConnectionFactory connectionFactory;

	public B_SharedDurableTopicConsumer(Topic topic, ConnectionFactory connectionFactory) {
		this.topic = topic;
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;

		try {
			jmsContext = connectionFactory.createContext();
			JMSConsumer jmsConsumer1 = jmsContext.createSharedDurableConsumer(topic, "SharedDurableConsumer1");
			JMSConsumer jmsConsumer2 = jmsContext.createSharedDurableConsumer(topic, "SharedDurableConsumer2");

			Message message = jmsConsumer1.receive(2000);
			Log.ReceivedMessage("SharedDurableConsumer1", message);
			message.acknowledge();

			message = jmsConsumer2.receive(2000);
			Log.ReceivedMessage("SharedDurableConsumer2", message);
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