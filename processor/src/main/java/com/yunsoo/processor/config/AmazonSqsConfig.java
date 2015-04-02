package com.yunsoo.processor.config;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/3/31
 * Descriptions:
 */
@Configuration
public class AmazonSqsConfig {

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQS amazonSqs, ResourceIdResolver resourceIdResolver) {
        return new QueueMessagingTemplate(amazonSqs, resourceIdResolver);
    }

}
