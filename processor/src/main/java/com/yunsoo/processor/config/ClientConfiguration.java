package com.yunsoo.processor.config;

import com.yunsoo.processor.client.DataApiClient;
import com.yunsoo.processor.client.FileApiClient;
import com.yunsoo.processor.client.KeyApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
@Configuration
public class ClientConfiguration {

    @Bean
    public DataApiClient dataApiClient(@Value("${yunsoo.client.data_api.base_url}") String dataApiBaseUrl) {
        return new DataApiClient(dataApiBaseUrl);
    }

    @Bean
    public FileApiClient fileApiClient(@Value("${yunsoo.client.file_api.base_url}") String fileApiBaseUrl) {
        return new FileApiClient(fileApiBaseUrl);
    }

    @Bean
    public KeyApiClient keyApiClient(@Value("${yunsoo.client.key_api.base_url}") String keyApiBaseUrl) {
        return new KeyApiClient(keyApiBaseUrl);
    }

}