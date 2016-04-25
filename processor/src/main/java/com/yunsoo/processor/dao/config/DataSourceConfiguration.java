package com.yunsoo.processor.dao.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

import javax.sql.DataSource;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
@Configuration
public class DataSourceConfiguration {

    private static Log log = LogFactory.getLog(DataSourceConfiguration.class);

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "jdbc.processor")
    public DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder.create().build();
        log.info("dataSource created. " + dataSource.getClass());
        return dataSource;
    }

    // Bean used to translate Hibernate exceptions into Spring ones
    @Bean
    @ConditionalOnMissingBean(PersistenceExceptionTranslationPostProcessor.class)
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        PersistenceExceptionTranslationPostProcessor postProcessor = new PersistenceExceptionTranslationPostProcessor();
        postProcessor.setProxyTargetClass(true);
        return postProcessor;
    }

}