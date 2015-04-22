package com.yunsoo.data.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Zhe on 2015/3/22.
 */
@Configuration(value = "dbsetting.master")
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jdbc.master")
public class DBSetting {
    private String driver_class;
    private String url;
    private String username;
    private String password;
    private String pool_initialsize;
    private String pool_maxidle;

    public String getDriver_class() {
        return driver_class;
    }

    public void setDriver_class(String driver_class) {
        this.driver_class = driver_class;
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

    public String getPool_initialsize() {
        return pool_initialsize;
    }

    public void setPool_initialsize(String pool_initialsize) {
        this.pool_initialsize = pool_initialsize;
    }

    public String getPool_maxidle() {
        return pool_maxidle;
    }

    public void setPool_maxidle(String pool_maxidle) {
        this.pool_maxidle = pool_maxidle;
    }
}
