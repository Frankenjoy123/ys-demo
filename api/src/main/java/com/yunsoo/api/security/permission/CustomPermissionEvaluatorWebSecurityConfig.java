package com.yunsoo.api.security.permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Created by yan on 7/24/2015.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomPermissionEvaluatorWebSecurityConfig extends GlobalMethodSecurityConfiguration  {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomPermissionEvaluatorWebSecurityConfig.class);

    public CustomPermissionEvaluatorWebSecurityConfig(){LOGGER.debug("init CustomPermissionEvaluatorWebSecurityConfig");}

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        LOGGER.debug("init createExpressionHandler");
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new BasePermissionEvaluator());
        return expressionHandler;
    }
}
