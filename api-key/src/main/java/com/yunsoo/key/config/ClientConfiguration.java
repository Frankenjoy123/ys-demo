package com.yunsoo.key.config;

import com.yunsoo.key.client.FileApiClient;
import com.yunsoo.key.client.ProcessorClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2016-08-19
 * Descriptions:
 */
@Configuration
public class ClientConfiguration {

    @Value("${yunsoo.client.processor.base_url}")
    private String processorBaseUrl;

    @Value("${yunsoo.client.file_api.base_url}")
    private String fileApiBaseUrl;


    @Bean
    public ProcessorClient processorClient() {
        return new ProcessorClient(processorBaseUrl);
    }

    @Bean
    public FileApiClient fileApiClient() {
        return new FileApiClient(fileApiBaseUrl);
    }

}
