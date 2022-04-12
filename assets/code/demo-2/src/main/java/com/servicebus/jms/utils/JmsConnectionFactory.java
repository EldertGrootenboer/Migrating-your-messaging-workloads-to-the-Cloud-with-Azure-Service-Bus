package com.servicebus.jms.utils;

import com.microsoft.azure.servicebus.jms.ServiceBusJmsConnectionFactory;
import com.microsoft.azure.servicebus.jms.ServiceBusJmsConnectionFactorySettings;

import javax.jms.ConnectionFactory;
import java.io.IOException;
import java.util.Properties;

public class JmsConnectionFactory {
	public static ConnectionFactory Get() throws IOException {
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		String connectionString = properties.getProperty("jms.connectionstring");

		// ActiveMQ
		//		ActiveMQJMSConnectionFactory connectionFactory = new ActiveMQJMSConnectionFactory(connectionString);

		// Service Bus
		ServiceBusJmsConnectionFactorySettings connFactorySettings = new ServiceBusJmsConnectionFactorySettings();
		connFactorySettings.setConnectionIdleTimeoutMS(20000);

		return new ServiceBusJmsConnectionFactory(connectionString, connFactorySettings);
	}
}