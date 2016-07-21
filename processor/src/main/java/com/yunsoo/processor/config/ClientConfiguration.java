package com.yunsoo.processor.config;

import com.yunsoo.processor.client.DataApiClient;
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

    @Value("${yunsoo.client.data_api.base_url}")
    private String dataApiBaseUrl;


    @Bean
    public DataApiClient dataAPIClient() {
        return new DataApiClient(formatBaseUrl(dataApiBaseUrl));
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