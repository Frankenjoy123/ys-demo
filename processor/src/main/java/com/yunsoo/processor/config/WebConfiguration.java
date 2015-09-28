package com.yunsoo.processor.config;

import com.yunsoo.common.web.util.CaseInsensitiveAntPathMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created by:   Lijian
 * Created on:   2015/4/27
 * Descriptions:
 */
@Configuration
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
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/resources/favicon.ico")
                .setCachePeriod(604800); //1 week
    }

}
