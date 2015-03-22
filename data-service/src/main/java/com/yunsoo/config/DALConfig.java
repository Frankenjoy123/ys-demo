package com.yunsoo.config;

/**
 * Created by:   Zhe
 * Created on:   2015/1/24
 * Descriptions:
 */
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

import javax.annotation.Resource;
import java.util.Properties;

// Needed by Spring to add this class to the ApplicationContext's configuration
@Configuration
@ComponentScan(basePackages = {"com.yunsoo.dataapi.config"})
// Property file in which are written the MySQL connection properties
@PropertySource("classpath:jdbc.properties")
public class DALConfig {

//    private static final String PROPERTY_NAME_DATABASE_DRIVER = "jdbc.driver_class";
//    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "jdbc.password";
//    private static final String PROPERTY_NAME_DATABASE_URL = "jdbc.url";
//    private static final String PROPERTY_NAME_DATABASE_USERNAME = "jdbc.username";
//    private static final String PROPERTY_NAME_POOL_INITIAL_SIZE = "pool.initialsize";
//    private static final String PROPERTY_NAME_POOL_MAX_IDLE = "pool.maxidle";

    // Needed to access property file
//    @Resource
//    private Environment environment;
    @Autowired
    private DBSetting dbSetting;

    // The bean which defines the BasicDataSource (DBCP)
    @Bean
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
