package com.servicebus.jms.utils;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

public class JmsQueue
{
	public static Queue Get(ConnectionFactory connectionFactory, String queueName)
	{
		JMSContext jmsContext = connectionFactory.createContext();		
		Queue queue = jmsContext.createQueue("B_DemoTopic");
		return queue;
	}
}