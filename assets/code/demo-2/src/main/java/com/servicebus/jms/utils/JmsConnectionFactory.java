package com.servicebus.jms.utils;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.microsoft.azure.servicebus.jms.ServiceBusJmsConnectionFactory;
import com.microsoft.azure.servicebus.jms.ServiceBusJmsConnectionFactorySettings;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.ConnectionFactory;
import java.io.IOException;
import java.util.Properties;

public class JmsConnectionFactory {
	public static ConnectionFactory Get() throws IOException {
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		String connectionString = properties.getProperty("jms.connectionstring");

		// ActiveMQ
//		return new ActiveMQJMSConnectionFactory(connectionString);

		// Service Bus
		TokenCredential tokenCredential = new DefaultAzureCredentialBuilder()
				.build();
		ServiceBusJmsConnectionFactorySettings connFactorySettings = new ServiceBusJmsConnectionFactorySettings();
		connFactorySettings.setConnectionIdleTimeoutMS(20000);

		return new ServiceBusJmsConnectionFactory(tokenCredential, connectionString, connFactorySettings);
	}
}