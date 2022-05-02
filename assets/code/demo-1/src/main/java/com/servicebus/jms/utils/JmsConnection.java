package com.servicebus.jms.utils;

import com.azure.messaging.servicebus.ServiceBusConnectionStringProperties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

public class JmsConnection {
	private final Destination destination;
	private final Connection connection;

	public JmsConnection() throws NamingException, JMSException, IOException {
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		String connectionString = properties.getProperty("jms.connectionstring");

//		String initialContextFactory = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
		String initialContextFactory = "org.apache.qpid.jms.jndi.JmsInitialContextFactory";
		ServiceBusConnectionStringProperties connectionStringProperties = ServiceBusConnectionStringProperties.parse(connectionString);

		Hashtable<String, String> hashtable = new Hashtable<>();

//		hashtable.put(Context.PROVIDER_URL, connectionString);
		hashtable.put("connectionfactory.SBCF", "amqps://" + connectionStringProperties.getFullyQualifiedNamespace() + "?amqp.idleTimeout=120000&amqp.traceFrames=true");

		hashtable.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		hashtable.put("queue.QUEUE", "DeliveryQueue");
		Context context = new InitialContext(hashtable);
		this.destination = (Destination) context.lookup("QUEUE");

//		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
//		this.connection = factory.createConnection();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("SBCF");
		this.connection = factory.createConnection(connectionStringProperties.getSharedAccessKeyName(), connectionStringProperties.getSharedAccessKey());
	}

	public Connection getConnection()
	{
		return connection;
	}

	public Destination getDestination()
	{
		return destination;
	}
}
