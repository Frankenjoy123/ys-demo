package com.yunsoo.marketing.config;

import com.yunsoo.marketing.client.AuthApiClient;
import com.yunsoo.marketing.client.FileApiClient;
import com.yunsoo.marketing.client.KeyApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2016-10-13
 * Descriptions:
 */
@Configuration
public class ClientConfiguration {

    @Bean
    public AuthApiClient authApiClient(@Value("${yunsoo.client.auth_api.base_url}") String authApiBaseUrl) {
        return new AuthApiClient(authApiBaseUrl);
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
