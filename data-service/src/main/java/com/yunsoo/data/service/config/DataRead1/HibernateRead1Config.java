package com.yunsoo.data.service.config.DataRead1;

import com.yunsoo.data.service.config.HibernateSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Zhe on 2015/4/20.
 */

@Configuration
//@Import(DBRead1Setting.class)
public class HibernateRead1Config {

    @Autowired
    @Qualifier(value = "datasource.read1")
    DataSource dataSource;
    @Autowired
    HibernateSetting hibernateSetting;

    // Bean which defines the FactoryBean for the SessionBean
    @Bean(name = "sessionfactory.read1")
//    @Qualifier(value = "sessionfactory.read1")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();

        lsfb.setPackagesToScan(hibernateSetting.getPackage_to_scan());
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", hibernateSetting.getDialect());
        hibernateProperties.put("hibernate.show_sql", hibernateSetting.getShow_sql());
        lsfb.setHibernateProperties(hibernateProperties);
        lsfb.setDataSource(dataSource);

        return lsfb;
    }

//    @Bean
//    public HibernateTransactionManager transactionManager() {
//        HibernateTransactionManager htm = new HibernateTransactionManager(sessionFactory().getObject());
//        return htm;
//    }
}