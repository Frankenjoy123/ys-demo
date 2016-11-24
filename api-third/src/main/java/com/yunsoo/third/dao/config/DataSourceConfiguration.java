package com.yunsoo.third.dao.config;

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

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "jdbc.third")
    public DataSource dataSource() throws Exception {
        DataSource dataSource = DataSourceBuilder.create().build();
        log.info("authDataSource created. " + dataSource.getClass());
        return dataSource;
    }

    // Bean used to translate Hibernate exceptions into Spring ones
    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        PersistenceExceptionTranslationPostProcessor postProcessor = new PersistenceExceptionTranslationPostProcessor();
        postProcessor.setProxyTargetClass(true);
        return postProcessor;
    }

}
