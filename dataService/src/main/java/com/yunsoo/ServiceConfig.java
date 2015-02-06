package com.yunsoo;

import com.yunsoo.service.Impl.ProductKeyServiceImpl;
import com.yunsoo.service.ProductKeyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by:   Lijian
 * Created on:   2015/2/5
 * Descriptions:
 */
@Configuration
@Import(DaoConfig.class)
public class ServiceConfig {

    @Bean
    public ProductKeyService productKeyService() {
        return new ProductKeyServiceImpl();
    }
}
