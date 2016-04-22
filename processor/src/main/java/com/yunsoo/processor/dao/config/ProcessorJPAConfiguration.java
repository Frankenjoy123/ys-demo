package com.yunsoo.processor.dao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
@EnableJpaRepositories(basePackages = "com.yunsoo.processor.dao.repository",
        entityManagerFactoryRef = "processorEntityManagerFactory",
        transactionManagerRef = "processorTransactionManager")
@EnableTransactionManagement
@Configuration
@Import({DataSourceConfiguration.class})
public class ProcessorJPAConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean processorEntityManagerFactory(EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.yunsoo.processor.dao.entity")
                .persistenceUnit("processor")
                .build();
    }

    @Bean
    public PlatformTransactionManager processorTransactionManager(@Qualifier("processorEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

}
