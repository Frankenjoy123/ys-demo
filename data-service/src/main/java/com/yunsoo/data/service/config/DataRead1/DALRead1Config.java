package com.yunsoo.data.service.config.DataRead1;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

import java.util.Properties;

/**
 * Created by Zhe on 2015/4/20.
 */

// Needed by Spring to add this class to the ApplicationContext's configuration
@Configuration
public class DALRead1Config {

    @Autowired
    private DBRead1Setting dbRead1Setting;

    // The bean which defines the BasicDataSource (DBCP)
    @Bean(name = "datasource.read1")
    @ConfigurationProperties(prefix = "datasource.read1")
    public BasicDataSource dataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", dbRead1Setting.getDriver_class());
        props.put("url", dbRead1Setting.getUrl());
        props.put("username", dbRead1Setting.getUsername());
        props.put("password", dbRead1Setting.getPassword());
        props.put("initialSize", dbRead1Setting.getPool_initialsize());
        props.put("maxIdle", dbRead1Setting.getPool_maxidle());
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
