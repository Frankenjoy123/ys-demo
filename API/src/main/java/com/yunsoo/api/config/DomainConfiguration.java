package com.yunsoo.api.config;

import com.yunsoo.api.domain.ProductKeyDomain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/3/17
 * Descriptions:
 */
@Configuration
public class DomainConfiguration {

    @Bean
    public ProductKeyDomain productKeyDomain() {
        return new ProductKeyDomain();
    }

}
