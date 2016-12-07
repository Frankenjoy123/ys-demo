package com.yunsoo.api.rabbit.config;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.client.*;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
@Configuration
public class ClientConfiguration {

    @Value("${yunsoo.client.auth_api.base_url}")
    private String authApiBaseUrl;

    @Value("${yunsoo.client.auth_api.system_account_access_token}")
    private String authApiSystemAccountAccessToken;

    @Value("${yunsoo.client.data_api.base_url}")
    private String dataApiBaseUrl;

    @Value("${yunsoo.client.file_api.base_url}")
    private String fileApiBaseUrl;

    @Value("${yunsoo.client.key_api.base_url}")
    private String keyApiBaseUrl;

    @Value("${yunsoo.client.third_api.base_url}")
    private String thirdApiBaseUrl;

    @Bean
    public AuthApiClient authApiClient() {
        AuthApiClient authApiClient = new AuthApiClient(authApiBaseUrl);
        authApiClient.setPreRequestCallback(request -> {
            HttpHeaders httpHeaders = request.getHeaders();
            try {
                httpHeaders.set(Constants.HttpHeaderName.APP_ID, Constants.Ids.API_RABBIT_APP_ID);
                //httpHeaders.set(Constants.HttpHeaderName.DEVICE_ID, null);
                httpHeaders.set(Constants.HttpHeaderName.ACCESS_TOKEN, authApiSystemAccountAccessToken);
            } catch (UnauthorizedException ignored) {
                //ignored if not login
            }
        });

        return authApiClient;
    }

    @Bean
    public DataApiClient dataApiClient() {
        return new DataApiClient(dataApiBaseUrl);
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
    public ThirdApiClient thirdApiClient() {
        return new ThirdApiClient(thirdApiBaseUrl);
    }
}