package com.yunsoo.dataapi.config;

/**
 * Created by:   Zhe
 * Created on:   2015/1/24
 * Descriptions:
 */
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
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

    private static final String PROPERTY_NAME_DATABASE_DRIVER = "jdbc.driver_class";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "jdbc.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "jdbc.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "jdbc.username";
    private static final String PROPERTY_NAME_POOL_INITIAL_SIZE = "pool.initialsize";
    private static final String PROPERTY_NAME_POOL_MAX_IDLE = "pool.maxidle";

    // Needed to access property file
    @Resource
    private Environment environment;

    // The bean which defines the BasicDataSource (DBCP)
    @Bean
    public BasicDataSource dataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", environment.getProperty(PROPERTY_NAME_DATABASE_DRIVER));
        props.put("url", environment.getProperty(PROPERTY_NAME_DATABASE_URL));
        props.put("username", environment.getProperty(PROPERTY_NAME_DATABASE_USERNAME));
        props.put("password", environment.getProperty(PROPERTY_NAME_DATABASE_PASSWORD));
        props.put("initialSize", environment.getProperty(PROPERTY_NAME_POOL_INITIAL_SIZE));
        props.put("maxIdle", environment.getProperty(PROPERTY_NAME_POOL_MAX_IDLE));
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
