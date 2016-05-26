package com.yunsoo.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2016-05-24
 * Descriptions:
 */
@Configuration
public class YSConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "yunsoo")
    public YSConfigProperties YSConfig() {
        return new YSConfigProperties();
    }

}
