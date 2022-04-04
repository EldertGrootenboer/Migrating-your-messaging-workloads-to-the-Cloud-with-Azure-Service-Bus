package com.servicebus.jms.utils;

import java.io.IOException;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Topic;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

public class JmsConnectionFactory
{
	public static ConnectionFactory Get() throws IOException
	{
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		String connectionString = properties.getProperty("jms.connectionstring");

		// ActiveMQ
		ActiveMQJMSConnectionFactory connectionFactory = new ActiveMQJMSConnectionFactory(connectionString);

		// Service Bus
//			ServiceBusJmsConnectionFactorySettings connFactorySettings = new ServiceBusJmsConnectionFactorySettings();
//			connFactorySettings.setConnectionIdleTimeoutMS(20000);
//			ServiceBusJmsConnectionFactory connectionFactory = new ServiceBusJmsConnectionFactory(connectionString, connFactorySettings);
		
		return connectionFactory;
	}
}