package com.yunsoo.processor.config;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/3/31
 * Descriptions:
 */
@Configuration
public class AmazonSqsConfiguration {

    @Value("${yunsoo.environment}")
    private String environment;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQS amazonSqs, ResourceIdResolver resourceIdResolver) {
        return new QueueMessagingTemplate(amazonSqs, resourceIdResolver);
    }

    @Bean
    public CustomQueueMessagingTemplate customQueueMessagingTemplate(AmazonSQS amazonSqs, ResourceIdResolver resourceIdResolver) {
        return new CustomQueueMessagingTemplate(amazonSqs, resourceIdResolver);
    }

    @Bean
    public ResourceIdResolver resourceIdResolver() {
        return new EnvironmentPrefixResourceIdResolver(environment);
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
