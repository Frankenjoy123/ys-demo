package com.yunsoo.processor.sqs.config;

import com.amazonaws.services.sqs.AmazonSQS;
import com.yunsoo.processor.config.AWSConfiguration;
import com.yunsoo.processor.sqs.CustomQueueMessagingTemplate;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by:   Lijian
 * Created on:   2016-04-27
 * Descriptions:
 */
@Configuration
@Import(AWSConfiguration.class)
public class SQSConfiguration {

    @Bean
    public CustomQueueMessagingTemplate customQueueMessagingTemplate(AmazonSQS amazonSqs, ResourceIdResolver resourceIdResolver) {
        return new CustomQueueMessagingTemplate(amazonSqs, resourceIdResolver);
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory() {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        //  factory.setAmazonSqs(amazonSqs);
        //  factory.setAutoStartup(false);
        factory.setMaxNumberOfMessages(5);

        return factory;
    }
}
