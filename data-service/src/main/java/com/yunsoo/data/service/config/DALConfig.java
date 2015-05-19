package com.yunsoo.data.service.config;

/**
 * Created by:   Zhe
 * Created on:   2015/1/24
 * Descriptions:
 */
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import java.util.Properties;

@Configuration
@Import(DBSetting.class)
public class DALConfig {

    @Autowired
    @Qualifier(value = "dbsetting.master")
    private DBSetting dbSetting;

    @Bean(name = "datasource.primary")
    @Primary
    @ConfigurationProperties(prefix = "datasource.primary")
    public BasicDataSource dataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", dbSetting.getDriver_class());
        props.put("url", dbSetting.getUrl());
        props.put("username", dbSetting.getUsername());
        props.put("password", dbSetting.getPassword());
        props.put("initialSize", dbSetting.getPool_initialsize());
        props.put("maxIdle", dbSetting.getPool_maxidle());
        BasicDataSource bds = BasicDataSourceFactory.createDataSource(props);
        return bds;
    }

    // Bean used to translate Hibernate's exceptions into Spring's ones
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        PersistenceExceptionTranslationPostProcessor b = new PersistenceExceptionTranslationPostProcessor();
        return b;
    }
}
