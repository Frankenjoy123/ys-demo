package com.yunsoo.data.api.config;

import com.yunsoo.common.web.util.CaseInsensitiveAntPathMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/1/28
 * Descriptions:
 */
@Configuration
//@EnableSpringDataWebSupport
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public CaseInsensitiveAntPathMatcher caseInsensitiveAntPathMatcher() {
        return new CaseInsensitiveAntPathMatcher();
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPathMatcher(caseInsensitiveAntPathMatcher()); //make path case insensitive.
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }
}
