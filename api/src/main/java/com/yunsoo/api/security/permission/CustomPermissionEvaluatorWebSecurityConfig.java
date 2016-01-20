package com.yunsoo.api.security.permission;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Created by:   yan
 * Created on:   7/24/2015
 * Descriptions:
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomPermissionEvaluatorWebSecurityConfig extends GlobalMethodSecurityConfiguration  {

    private Log log = LogFactory.getLog(this.getClass());

    public CustomPermissionEvaluatorWebSecurityConfig() {
        log.debug("init CustomPermissionEvaluatorWebSecurityConfig");
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        log.debug("init createExpressionHandler");
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new BasePermissionEvaluator());
        return expressionHandler;
    }
}
