package com.yunsoo.api.rabbit.config;

import com.yunsoo.api.rabbit.client.DataAPIClient;
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

    @Value("${yunsoo.api.client.dataapi.baseurl}")
    private String dataAPIBaseUrl;

    @Bean
    public DataAPIClient dataAPIClient() {
        return new DataAPIClient(formatBaseUrl(dataAPIBaseUrl));
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