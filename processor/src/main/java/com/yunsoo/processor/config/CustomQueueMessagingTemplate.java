package com.yunsoo.processor.config;

import com.amazonaws.services.sqs.AmazonSQS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.core.QueueMessageChannel;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by yan on 8/28/2015.
 */

public class CustomQueueMessagingTemplate extends QueueMessagingTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomQueueMessagingTemplate.class);

    public CustomQueueMessagingTemplate(AmazonSQS amazonSqs, ResourceIdResolver resourceIdResolver) {
        super(amazonSqs, resourceIdResolver);

    }

    public <T> void convertAndSend(String destinationName, T payload, Map<String, Object> headers, Long delaySeconds) throws MessagingException {
        QueueMessageChannel channel = resolveMessageChannelByLogicalName(destinationName);
        Message<?> message = doConvert(payload, headers, null);

        boolean success = channel.send(message, delaySeconds);
        if(!success){
            LOGGER.error("Send message to SQS failed. message body: " + String.valueOf(message.getPayload()));
        }

    }

}
