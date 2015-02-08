package com.yunsoo;

import com.yunsoo.dao.ProductKeyBatchDao;
import com.yunsoo.dao.ProductKeyDao;
import com.yunsoo.dao.impl.ProductKeyBatchDaoImpl;
import com.yunsoo.dao.impl.ProductKeyDaoImpl;
import com.yunsoo.nosql.dynamoDB.AmazonDynamoDBConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by:   Lijian
 * Created on:   2015/2/5
 * Descriptions:
 */
@Configuration
@Import(AmazonDynamoDBConfig.class)
public class DaoConfig {

    @Bean
    public ProductKeyDao productKeyDao(){
        return new ProductKeyDaoImpl();
    }

    @Bean
    public ProductKeyBatchDao productKeyBatchDao(){
        return new ProductKeyBatchDaoImpl();
    }
}
