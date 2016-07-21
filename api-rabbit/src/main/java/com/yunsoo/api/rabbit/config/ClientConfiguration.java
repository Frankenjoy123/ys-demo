package com.yunsoo.api.rabbit.config;

import com.yunsoo.api.rabbit.client.DataApiClient;
import com.yunsoo.api.rabbit.client.WeChatApiClient;
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
    public DataApiClient dataApiClient() {
        return new DataApiClient(dataApiBaseUrl);
    }

    @Bean
    public WeChatApiClient weChatApiClient() {
        return new WeChatApiClient("https://api.weixin.qq.com/cgi-bin/");
    }

}