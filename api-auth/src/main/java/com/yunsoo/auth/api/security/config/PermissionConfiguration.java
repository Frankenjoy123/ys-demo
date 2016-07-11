package com.yunsoo.auth.api.security.config;

import com.yunsoo.auth.api.security.SimplePermissionEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Created by:   Lijian
 * Created on:   2016-03-30
 * Descriptions:
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PermissionConfiguration {

    @Bean
    public SimplePermissionEvaluator simplePermissionEvaluator() {
        return new SimplePermissionEvaluator();
    }

}
