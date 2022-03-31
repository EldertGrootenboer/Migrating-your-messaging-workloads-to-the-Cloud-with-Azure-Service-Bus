package com.servicebus.jms.main;

import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import com.microsoft.azure.servicebus.jms.ServiceBusJmsConnectionFactory;
import com.microsoft.azure.servicebus.jms.ServiceBusJmsConnectionFactorySettings;
import com.servicebus.jms.consumer.FilteredTopicConsumer;
import com.servicebus.jms.consumer.QueueConsumer;
import com.servicebus.jms.consumer.SharedDurableTopicConsumer;
import com.servicebus.jms.consumer.TemporaryQueueConsumer;
import com.servicebus.jms.producer.FilteredTopicProducer;
import com.servicebus.jms.producer.QueueProducer;
import com.servicebus.jms.producer.TemporaryQueueProducer;
import com.servicebus.jms.producer.TopicProducer;
import com.servicebus.jms.utils.Log;

public class Demo {
	public static void main(final String[] args) throws Exception {
		new Demo().runExample();
	}

	public boolean runExample() throws Exception {
		InitialContext initialContext = null;
		try {
			Properties properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			
			// ActiveMQ
			ActiveMQJMSConnectionFactory connectionFactory = new ActiveMQJMSConnectionFactory("tcp://localhost:61616");
			
			// Service Bus
//			String ServiceBusConnectionString = properties.getProperty("sb.connectionstring");
//			ServiceBusJmsConnectionFactorySettings connFactorySettings = new ServiceBusJmsConnectionFactorySettings();
//			connFactorySettings.setConnectionIdleTimeoutMS(20000);
//			ServiceBusJmsConnectionFactory connectionFactory = new ServiceBusJmsConnectionFactory(ServiceBusConnectionString, connFactorySettings);
			
			Log.Section("Simple queue");
			QueueProducer queueProducer = new QueueProducer(connectionFactory);
			queueProducer.run();

			QueueConsumer queueConsumer = new QueueConsumer(queueProducer.GetQueue(), connectionFactory);
			queueConsumer.run();

			Log.Section("Simple topic with 2 subscribers");
			TopicProducer topicProducer = new TopicProducer(connectionFactory);
			topicProducer.run();

			SharedDurableTopicConsumer sharedDurableTopicConsumer = new SharedDurableTopicConsumer(topicProducer.GetTopic(), connectionFactory);
			sharedDurableTopicConsumer.run();

			Log.Section("Request-response with temporary queue");
			TemporaryQueueProducer temporaryQeueProducer = new TemporaryQueueProducer(connectionFactory);
			Thread temporaryQeueProducerThread = new Thread(temporaryQeueProducer);
			temporaryQeueProducerThread.start();
			
			Thread.sleep(2000);

			TemporaryQueueConsumer temporaryQueueConsumer = new TemporaryQueueConsumer(temporaryQeueProducer.GetQueue(), connectionFactory);
			temporaryQueueConsumer.run();

			Log.Section("Topic with 2 filtered subscribers");
			FilteredTopicProducer filteredTopicProducer = new FilteredTopicProducer(connectionFactory);
			filteredTopicProducer.run();

			FilteredTopicConsumer filteredTopicConsumer = new FilteredTopicConsumer(filteredTopicProducer.GetTopic(), connectionFactory);
			filteredTopicConsumer.run();

			return true;
		} finally {
			if (initialContext != null) {
				initialContext.close();
			}
		}
	}
}