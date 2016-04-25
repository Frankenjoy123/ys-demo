package com.yunsoo.processor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
@Configuration
public class ProcessorConfiguration {

    @Value("${yunsoo.environment}")
    private String environment;

    @Value("${yunsoo.debug}")
    private boolean debug;

    @Bean
    @ConfigurationProperties(prefix = "yunsoo.processor")
    public ProcessorConfigProperties processorConfigProperties() {
        ProcessorConfigProperties processorConfigProperties = new ProcessorConfigProperties();
        processorConfigProperties.setName(getHostName());
        processorConfigProperties.setEnvironment(environment);
        processorConfigProperties.setDebug(debug);
        return processorConfigProperties;
    }


    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown";
        }
    }

}
