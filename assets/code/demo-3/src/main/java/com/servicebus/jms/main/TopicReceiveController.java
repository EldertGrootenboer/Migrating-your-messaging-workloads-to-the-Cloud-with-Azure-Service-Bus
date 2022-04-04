package com.servicebus.jms.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicReceiveController {

    private static final String TOPIC_NAME = "UserTopic";

    private static final String USER_SUBSCRIPTION_NAME = "UserConsumer";
    private static final String ADMIN_SUBSCRIPTION_NAME = "AdminConsumer";

    private final Logger logger = LoggerFactory.getLogger(TopicReceiveController.class);

    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory", subscription = USER_SUBSCRIPTION_NAME, selector = "UserType = 'User'")
    public void receiveUserMessage(User user) {
        logger.info("Received user: {}", user.getName());
    }

    @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory", subscription = ADMIN_SUBSCRIPTION_NAME, selector = "UserType = 'Admin'")
    public void receiveAdminMessage(User admin) {
        logger.info("Received admin: {}", admin.getName());
    }
}