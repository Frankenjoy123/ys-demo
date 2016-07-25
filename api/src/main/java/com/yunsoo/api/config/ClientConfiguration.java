package com.yunsoo.api.config;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.api.client.ProcessorClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
@Configuration
@Import(PropertyPlaceholderAutoConfiguration.class)
public class ClientConfiguration {

    @Value("${yunsoo.client.dataapi.baseurl}")
    private String dataAPIBaseUrl;

    @Value("${yunsoo.client.processor.baseurl}")
    private String processorBaseUrl;


    @Bean
    public DataAPIClient dataAPIClient() {
        return new DataAPIClient(formatBaseUrl(dataAPIBaseUrl));
    }

    @Bean
    public ProcessorClient processorClient() {
        return new ProcessorClient(formatBaseUrl(processorBaseUrl));
    }


    private String formatBaseUrl(String baseUrl) {
        if (baseUrl == null) {
            baseUrl = "/";
        } else if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl;
    }

}