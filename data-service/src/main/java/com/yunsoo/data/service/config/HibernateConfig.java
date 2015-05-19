package com.yunsoo.data.service.config;

/**
 * Created by:   Zhe
 * Created on:   2015/1/24
 * Descriptions:
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Import(DBSetting.class)
public class HibernateConfig {

    @Autowired
    @Qualifier(value = "datasource.primary")
    DataSource dataSource;
    @Autowired
    HibernateSetting hibernateSetting;

    // Bean which defines the FactoryBean for the SessionBean
    @Bean(name = "sessionfactory.primary")
    @Primary
//    @Qualifier(value = "sessionfactory.primary")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();

        lsfb.setPackagesToScan(hibernateSetting.getPackage_to_scan());
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