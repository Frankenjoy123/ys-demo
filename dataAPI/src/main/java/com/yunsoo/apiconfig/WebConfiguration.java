package com.yunsoo.apiconfig;

import com.yunsoo.controller.util.CaseInsensitivePathMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.file.PathMatcher;

/**
 * Created by Zhe on 2015/1/28.
 */
@Configuration
@ComponentScan(basePackages = "com.yunsoo.apiconfig")
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public CaseInsensitivePathMatcher pathMatcher() {
        return new CaseInsensitivePathMatcher();
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setInterceptors(getInterceptors());
        handlerMapping.setPathMatcher(pathMatcher()); //make path case insensitive.
        return handlerMapping;
    }
}
