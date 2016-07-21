package com.yunsoo.api.config;

import com.yunsoo.api.client.AuthApiClient;
import com.yunsoo.api.client.DataApiClient1;
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

    @Value("${yunsoo.client.auth_api.base_url}")
    private String authApiBaseUrl;

    @Value("${yunsoo.client.data_api.base_url}")
    private String dataAPIBaseUrl;

    @Value("${yunsoo.client.processor.base_url}")
    private String processorBaseUrl;

    @Bean
    public AuthApiClient authApiClient() {
        return new AuthApiClient(authApiBaseUrl);
    }

    @Bean
    public DataApiClient1 dataAPIClient() {
        return new DataApiClient1(dataAPIBaseUrl);
    }

    @Bean
    public ProcessorClient processorClient() {
        return new ProcessorClient(processorBaseUrl);
    }

}