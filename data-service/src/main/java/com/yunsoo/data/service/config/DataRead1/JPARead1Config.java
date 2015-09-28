package com.yunsoo.data.service.config.DataRead1;

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
