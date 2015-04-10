package com.yunsoo.data.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 新项目用Spring boot，选择YAML配置，优先于传统的properties文件。
 * Created by Zhe on 2015/3/20.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "yunsoo")
public class DataAPISetting {


    private String debug;


    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }
}

