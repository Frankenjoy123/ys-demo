package com.yunsoo.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/8/12
 * Descriptions:
 */
@Configuration
@ConditionalOnAwsCloudEnvironment
public class AWSEnvironmentConfiguration {

    @Value("${yunsoo.environment}")
    private String environment;

    @Bean
    public ResourceIdResolver resourceIdResolver() {
        return new EnvironmentPrefixResourceIdResolver(environment);
    }
}
