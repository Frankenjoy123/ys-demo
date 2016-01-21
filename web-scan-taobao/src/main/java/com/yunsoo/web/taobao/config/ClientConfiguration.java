package com.yunsoo.web.taobao.config;

import com.yunsoo.web.taobao.client.ApiRabbitClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */

@Configuration
@Import(PropertyPlaceholderAutoConfiguration.class)
public class ClientConfiguration {

    @Value("${yunsoo.client.api-rabbit.baseurl}")
    private String apiRabbitBaseUrl;


    @Bean
    public ApiRabbitClient apiRabbitClient() {
        return new ApiRabbitClient(formatBaseUrl(apiRabbitBaseUrl));
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
