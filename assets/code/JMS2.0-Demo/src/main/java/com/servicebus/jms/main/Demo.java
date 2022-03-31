package com.servicebus.jms.main;

import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import com.microsoft.azure.servicebus.jms.ServiceBusJmsConnectionFactory;
import com.microsoft.azure.servicebus.jms.ServiceBusJmsConnectionFactorySettings;
import com.servicebus.jms.consumer.D_FilteredTopicConsumer;
import com.servicebus.jms.consumer.E_ScheduledMessageQueueConsumer;
import com.servicebus.jms.consumer.A_QueueConsumer;
import com.servicebus.jms.consumer.B_SharedDurableTopicConsumer;
import com.servicebus.jms.consumer.C_TemporaryQueueConsumer;
import com.servicebus.jms.producer.D_FilteredTopicProducer;
import com.servicebus.jms.producer.E_ScheduledMessageQueueProducer;
import com.servicebus.jms.producer.A_QueueProducer;
import com.servicebus.jms.producer.C_TemporaryQueueProducer;
import com.servicebus.jms.producer.B_TopicProducer;
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
			A_QueueProducer queueProducer = new A_QueueProducer(connectionFactory);
			queueProducer.run();

			A_QueueConsumer queueConsumer = new A_QueueConsumer(queueProducer.GetQueue(), connectionFactory);
			queueConsumer.run();

			Log.Section("Simple topic with 2 subscribers");
			B_TopicProducer topicProducer = new B_TopicProducer(connectionFactory);
			topicProducer.run();

			B_SharedDurableTopicConsumer sharedDurableTopicConsumer = new B_SharedDurableTopicConsumer(topicProducer.GetTopic(), connectionFactory);
			sharedDurableTopicConsumer.run();

			Log.Section("Request-response with temporary queue");
			C_TemporaryQueueProducer temporaryQeueProducer = new C_TemporaryQueueProducer(connectionFactory);
			Thread temporaryQeueProducerThread = new Thread(temporaryQeueProducer);
			temporaryQeueProducerThread.start();

			Thread.sleep(500);

			C_TemporaryQueueConsumer temporaryQueueConsumer = new C_TemporaryQueueConsumer(temporaryQeueProducer.GetQueue(), connectionFactory);
			temporaryQueueConsumer.run();

			Log.Section("Topic with 2 filtered subscribers");
			D_FilteredTopicProducer filteredTopicProducer = new D_FilteredTopicProducer(connectionFactory);
			filteredTopicProducer.run();

			D_FilteredTopicConsumer filteredTopicConsumer = new D_FilteredTopicConsumer(filteredTopicProducer.GetTopic(), connectionFactory);
			filteredTopicConsumer.run();
			
			Log.Section("Queue with scheduled message");
			E_ScheduledMessageQueueProducer scheduledMessageQueueProducer = new E_ScheduledMessageQueueProducer(connectionFactory);
			scheduledMessageQueueProducer.run();

			E_ScheduledMessageQueueConsumer scheduledMessageQueueConsumer = new E_ScheduledMessageQueueConsumer(scheduledMessageQueueProducer.GetQueue(), connectionFactory);
			scheduledMessageQueueConsumer.run();

			return true;
		} finally {
			if (initialContext != null) {
				initialContext.close();
			}
		}
	}
}