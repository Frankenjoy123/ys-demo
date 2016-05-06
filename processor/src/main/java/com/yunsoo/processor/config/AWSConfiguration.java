package com.yunsoo.processor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/3/31
 * Descriptions:
 */
@Configuration
public class AWSConfiguration {

    @Value("${yunsoo.environment}")
    private String environment;

    @Bean
    public ResourceIdResolver resourceIdResolver() {
        return new EnvironmentPrefixResourceIdResolver(environment);
    }

}
