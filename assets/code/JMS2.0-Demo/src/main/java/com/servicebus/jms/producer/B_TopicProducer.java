package com.servicebus.jms.producer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;

import com.servicebus.jms.utils.Log;

public class B_TopicProducer implements Runnable {
	private ConnectionFactory connectionFactory;
	private Topic topic;

	public B_TopicProducer(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;

		try {
			jmsContext = connectionFactory.createContext();
			
			topic = jmsContext.createTopic("jmsDemoTopic");
						
			JMSProducer jmsProducer = jmsContext.createProducer();
			
			String message = "Hello World!";
			jmsProducer.send(topic, message);
			Log.SentMessage(topic.getTopicName(), message);
		} catch (Exception excep) {
			excep.printStackTrace();
		} finally {
			if (jmsContext != null) {
				jmsContext.close();
			}
		}
	}
	
	public Topic GetTopic() {
		return topic;
	}
}