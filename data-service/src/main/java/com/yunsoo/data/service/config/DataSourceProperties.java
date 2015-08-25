package com.yunsoo.data.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

/**
 * Created by:   Lijian
 * Created on:   2015/8/25
 * Descriptions:
 */
@Configuration
@EnableConfigurationProperties()
@ConfigurationProperties(prefix = "jdbc.master")
public class DataSourceProperties {

    @NotNull(message = "driverClassName not configured")
    private String driverClassName;

    @NotNull(message = "url not configured")
    private String url;

    @NotNull(message = "username not configured")
    private String username;

    @NotNull(message = "password not configured")
    private String password;

    private String initialSize;

    private String maxIdle;


    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(String initialSize) {
        this.initialSize = initialSize;
    }

    public String getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(String maxIdle) {
        this.maxIdle = maxIdle;
    }
}
