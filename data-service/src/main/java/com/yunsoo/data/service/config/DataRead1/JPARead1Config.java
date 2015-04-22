package com.yunsoo.data.service.config.DataRead1;

import com.yunsoo.data.service.config.DALConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by Zhe on 2015/4/21.
 */
//@Configuration
//@Import(DALRead1Config.class)
//@EnableJpaRepositories(basePackages = "com.yunsoo.data.service.config.DataRead1")
public class JPARead1Config {
//    @Autowired
//    @Qualifier(value = "datasource.read1")
//    DataSource dataSource;
//
//    @Autowired
//    private HibernateJpaVendorAdapter hibernateJpaVendorAdapter;
//
//    @Bean(name = "entitymanagerfactory.read1")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//
//        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//        factory.setJpaVendorAdapter(hibernateJpaVendorAdapter);
//        factory.setPackagesToScan("com.yunsoo.data.service.entity");
//        factory.setDataSource(dataSource);
//        factory.setPersistenceUnitName("read1");
//        return factory;
//    }
//
//    @Bean(name = "transactionmanager.read1")
//    public PlatformTransactionManager transactionManager() {
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return txManager;
//    }
}
