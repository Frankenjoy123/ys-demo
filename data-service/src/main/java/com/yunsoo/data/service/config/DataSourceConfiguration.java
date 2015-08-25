package com.yunsoo.data.service.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

import java.util.Properties;

/**
 * Created by:   Lijian
 * Created on:   2015/8/25
 * Descriptions:
 */
@Configuration
@Import(DataSourceProperties.class)
public class DataSourceConfiguration {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean(name = "dataSource.primary")
    public BasicDataSource dataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", dataSourceProperties.getDriverClassName());
        props.put("url", dataSourceProperties.getUrl());
        props.put("username", dataSourceProperties.getUsername());
        props.put("password", dataSourceProperties.getPassword());
        props.put("initialSize", dataSourceProperties.getInitialSize());
        props.put("maxIdle", dataSourceProperties.getMaxIdle());
        return BasicDataSourceFactory.createDataSource(props);
    }

    // Bean used to translate Hibernate exceptions into Spring ones
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
