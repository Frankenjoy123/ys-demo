package com.yunsoo.web.taobao.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
@Configuration
@Import({ClientConfiguration.class})
@ComponentScan(basePackages = "com.yunsoo.web.taobao.domain")
public class DomainConfiguration {
}