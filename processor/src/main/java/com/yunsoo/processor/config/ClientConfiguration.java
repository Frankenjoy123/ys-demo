package com.yunsoo.processor.config;

import com.yunsoo.processor.client.DataApiClient1;
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

    @Value("${yunsoo.client.dataapi.baseurl}")
    private String dataAPIBaseUrl;


    @Bean
    public DataApiClient1 dataAPIClient() {
        return new DataApiClient1(formatBaseUrl(dataAPIBaseUrl));
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