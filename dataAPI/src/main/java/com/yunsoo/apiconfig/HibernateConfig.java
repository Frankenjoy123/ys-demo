package com.yunsoo.apiconfig;

/**
 * Created by Zhe on 2015/1/24.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.yunsoo.apiconfig"})
@PropertySource("classpath:jdbc.properties")
public class HibernateConfig {

    private static final String PROPERTY_NAME_DAL_CLASSES_PACKAGE = "com.yunsoo.dbmodel";
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "org.hibernate.dialect.MySQLDialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOWSQL = "true";
    @Resource
    private Environment environment;

    @Autowired
    DataSource dataSource;

    // Bean which defines the FactoryBean for the SessionBean
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();

        lsfb.setPackagesToScan(PROPERTY_NAME_DAL_CLASSES_PACKAGE);
        System.out.println("PROPERTY_NAME_DAL_CLASSES_PACKAGE: " + PROPERTY_NAME_DAL_CLASSES_PACKAGE);
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("dialect", PROPERTY_NAME_HIBERNATE_DIALECT);
        hibernateProperties.put("show_sql", PROPERTY_NAME_HIBERNATE_SHOWSQL);
        lsfb.setHibernateProperties(hibernateProperties);
        lsfb.setDataSource(dataSource);

//        lsfb.setConfigLocation("classpath:hibernate.cfg.xml.bc");
        return lsfb;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager htm = new HibernateTransactionManager(sessionFactory().getObject());
        return htm;
    }
}