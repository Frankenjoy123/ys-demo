package com.yunsoo.data.service.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

import javax.sql.DataSource;

/**
 * Created by:   Lijian
 * Created on:   2015/8/25
 * Descriptions:
 */
@Configuration
public class DataSourceConfiguration {

    private static Log log = LogFactory.getLog(DataSourceConfiguration.class);

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "jdbc.master")
    public DataSource dataSource() throws Exception {
//        Properties props = new Properties();
//        props.put("driverClassName", dataSourceProperties.getDriverClassName());
//        props.put("url", dataSourceProperties.getUrl());
//        props.put("username", dataSourceProperties.getUsername());
//        props.put("password", dataSourceProperties.getPassword());
//        props.put("initialSize", dataSourceProperties.getInitialSize());
//        props.put("maxIdle", dataSourceProperties.getMaxIdle());
//        return BasicDataSourceFactory.createDataSource(props);
        DataSource dataSource = DataSourceBuilder.create().build();
        log.info("dataSource created. " + dataSource.getClass());
        return dataSource;
    }

    // Bean used to translate Hibernate exceptions into Spring ones
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

}
