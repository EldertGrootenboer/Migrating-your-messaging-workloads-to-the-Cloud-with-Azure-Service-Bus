package com.servicebus.jms.utils;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;

public class JmsConnection {
	private Destination destination;
	private Connection connection;

	public JmsConnection() throws NamingException, JMSException, IOException {
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		String connectionString = properties.getProperty("jms.connectionstring");
		
//		String initialContextFactory = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";

		String initialContextFactory = "org.apache.qpid.jms.jndi.JmsInitialContextFactory";
		ConnectionStringBuilder csb = new ConnectionStringBuilder(connectionString);

		Hashtable<String, String> hashtable = new Hashtable<>();
		
//		hashtable.put(Context.PROVIDER_URL, connectionString);
		hashtable.put("connectionfactory.SBCF", "amqps://" + csb.getEndpoint().getHost() + "?amqp.idleTimeout=120000&amqp.traceFrames=true");
		
		hashtable.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
		hashtable.put("queue.QUEUE", "DeliveryQueue");
		Context context = new InitialContext(hashtable);
		this.destination = (Destination) context.lookup("QUEUE");

//		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
//		this.connection = factory.createConnection();

		ConnectionFactory factory = (ConnectionFactory) context.lookup("SBCF");
		this.connection = factory.createConnection(csb.getSasKeyName(), csb.getSasKey());
	}
	
	public Connection GetConnection()
	{
		return connection;
	}
	
	public Destination GetDestination()
	{
		return destination;
	}
}
