package com.yunsoo.api.rabbit.security.permission;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;

/**
 * Created by Zhe on 2015/3/6.
 * Purpose: PermissionEvaluator and other Permission related Bean's container class.
 */
@Configuration
public class PermissionConfig {

    @Bean
    public PermissionEvaluator permissionEvaluator() {
        BasePermissionEvaluator bean = new BasePermissionEvaluator();
        return bean;
    }

}
