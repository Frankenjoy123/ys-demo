package com.yunsoo.data.service.config;

/**
 * Created by:   Zhe
 * Created on:   2015/1/24
 * Descriptions:
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
//@ComponentScan(basePackages = {"com.yunsoo.data.service.dbmodel"})
//@PropertySource("classpath:jdbc.properties")
public class HibernateConfig {

    @Autowired
    private Environment environment;
    @Autowired
    DataSource dataSource;
    @Autowired
    HibernateSetting hibernateSetting;

    // Bean which defines the FactoryBean for the SessionBean
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();

        lsfb.setPackagesToScan(hibernateSetting.getPackage_to_scan());
//        System.out.println("PROPERTY_NAME_DAL_CLASSES_PACKAGE: " + hibernateSetting.getPackage_to_scan());
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", hibernateSetting.getDialect());
        hibernateProperties.put("hibernate.show_sql", hibernateSetting.getShow_sql());
        hibernateProperties.put("hibernate.hbm2ddl.auto", hibernateSetting.getHbm2ddl_auto());
        lsfb.setHibernateProperties(hibernateProperties);
        lsfb.setDataSource(dataSource);

        return lsfb;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager htm = new HibernateTransactionManager(sessionFactory().getObject());
        return htm;
    }
}