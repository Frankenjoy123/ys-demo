package com.yunsoo.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by:   Lijian
 * Created on:   2015/3/17
 * Descriptions:
 */
@Configuration
@Import({ClientConfiguration.class, YunsooYamlConfig.class})
@ComponentScan(basePackages = "com.yunsoo.api.domain")
public class DomainConfiguration {
}
