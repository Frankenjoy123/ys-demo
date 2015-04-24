package com.yunsoo.processor.config;

import com.amazonaws.services.sqs.AmazonSQS;
import org.springframework.beans.factory.annotation.Value;
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
public class AmazonSqsConfiguration {

    @Value("${yunsoo.environment}")
    private String environment;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQS amazonSqs) {
        return new QueueMessagingTemplate(amazonSqs, this::resolve);
    }


    private String resolve(String logicalResourceId) {
        return environment + "-" + logicalResourceId;
    }

}
