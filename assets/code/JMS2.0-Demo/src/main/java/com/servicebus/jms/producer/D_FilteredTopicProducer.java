package com.servicebus.jms.producer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Topic;

import com.servicebus.jms.utils.Log;

public class D_FilteredTopicProducer implements Runnable {
	private ConnectionFactory connectionFactory;
	private Topic topic;

	public D_FilteredTopicProducer(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void run() {
		JMSContext jmsContext = null;

		try {
			jmsContext = connectionFactory.createContext();
			
			topic = jmsContext.createTopic("D_DemoFilteredTopic");
						
			JMSProducer jmsProducer = jmsContext.createProducer();
			
			Message message = jmsContext.createTextMessage("Hello North America!");
			message.setStringProperty("Region", "NorthAmerica");			
			jmsProducer.send(topic, message);
			Log.SentMessage(topic.getTopicName(), message);

			message = jmsContext.createTextMessage("Hello Europe!");
			message.setStringProperty("Region", "Europe");			
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