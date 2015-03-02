package com.yunsoo.api.config;

import com.yunsoo.api.controller.util.CaseInsensitivePathMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Created by:   Zhe
 * Created on:   2015/1/28
 * Descriptions:
 */
@Configuration
@ComponentScan(basePackages = "com.yunsoo.api.config")
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public CaseInsensitivePathMatcher pathMatcher() {
        return new CaseInsensitivePathMatcher();
    }

//    @Bean
//    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
//        handlerMapping.setOrder(0);
//        handlerMapping.setInterceptors(getInterceptors());
//        handlerMapping.setPathMatcher(pathMatcher()); //make path case insensitive.
//        return handlerMapping;
//    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPathMatcher(pathMatcher()); //make path case insensitive.
    }
}
