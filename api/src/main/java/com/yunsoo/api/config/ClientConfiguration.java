package com.yunsoo.api.config;

import com.yunsoo.api.client.*;
import com.yunsoo.api.security.AuthDetails;
import com.yunsoo.api.security.authentication.AccountAuthentication;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.web.Constants;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

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

    @Value("${yunsoo.client.file_api.base_url}")
    private String fileApiBaseUrl;

    @Value("${yunsoo.client.key_api.base_url}")
    private String keyApiBaseUrl;

    @Value("${yunsoo.client.di_api.base_url}")
    private String diApiBaseUrl;

    @Bean
    public AuthApiClient authApiClient() {
        AuthApiClient authApiClient = new AuthApiClient(authApiBaseUrl);
        authApiClient.setPreRequestCallback(request -> {
            HttpHeaders httpHeaders = request.getHeaders();
            try {
                AccountAuthentication authentication = AuthUtils.getAuthentication();
                AuthDetails authDetails = authentication.getDetails();
                httpHeaders.set(Constants.HttpHeaderName.APP_ID, authDetails.getAppId());
                httpHeaders.set(Constants.HttpHeaderName.DEVICE_ID, authDetails.getDeviceId());
                httpHeaders.set(Constants.HttpHeaderName.ACCESS_TOKEN, authentication.getCredentials());
            } catch (UnauthorizedException ignored) {
                //ignored if not login
            }
        });

        return authApiClient;
    }

    @Bean
    public DataApiClient dataApiClient() {
        return new DataApiClient(dataAPIBaseUrl);
    }

    @Bean
    public ProcessorClient processorClient() {
        return new ProcessorClient(processorBaseUrl);
    }

    @Bean
    public WeChatApiClient weChatApiClient() {
        return new WeChatApiClient("https://api.weixin.qq.com/cgi-bin/");
    }

    @Bean
    public FileApiClient fileApiClient() {
        return new FileApiClient(fileApiBaseUrl);
    }

    @Bean
    public KeyApiClient keyApiClient() {
        return new KeyApiClient(keyApiBaseUrl);
    }

    @Bean
    public DiApiClient diApiClient() {
        return new DiApiClient(diApiBaseUrl);
    }
}