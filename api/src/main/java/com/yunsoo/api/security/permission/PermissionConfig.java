package com.yunsoo.api.security.permission;

import com.yunsoo.api.AppContext;
import com.yunsoo.api.domain.PermissionDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * Created by Zhe on 2015/3/6.
 * Purpose: PermissionEvaluator and other Permission related Bean's container class.
 */

@Configuration
public class PermissionConfig {

    /*
    @Bean
    public PermissionEvaluator permissionEvaluator() {
        BasePermissionEvaluator bean = new BasePermissionEvaluator();
        return bean;
    }
*/
}

