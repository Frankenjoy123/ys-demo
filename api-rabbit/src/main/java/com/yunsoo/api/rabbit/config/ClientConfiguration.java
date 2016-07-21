package com.yunsoo.api.rabbit.config;

import com.yunsoo.api.rabbit.client.DataApiClient1;
import com.yunsoo.api.rabbit.client.WXAPIClient;
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

    @Bean
    public WXAPIClient wxAPIClient() {
        return new WXAPIClient(formatBaseUrl("https://api.weixin.qq.com/cgi-bin"));
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