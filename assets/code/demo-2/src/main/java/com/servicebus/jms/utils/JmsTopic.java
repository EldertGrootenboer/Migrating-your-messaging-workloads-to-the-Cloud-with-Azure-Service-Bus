package com.servicebus.jms.utils;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Topic;

public class JmsTopic
{
	public static Topic Get(ConnectionFactory connectionFactory, String topicName)
	{
		JMSContext jmsContext = connectionFactory.createContext();		
		Topic topic = jmsContext.createTopic(topicName);
		return topic;
	}
	
	public static Topic Get(ConnectionFactory connectionFactory, String topicName, String filter)
	{
		JMSContext jmsContext = connectionFactory.createContext();
		Topic topic = jmsContext.createTopic(topicName);
		return topic;
	}
}