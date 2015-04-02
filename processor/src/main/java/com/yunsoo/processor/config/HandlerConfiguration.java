package com.yunsoo.processor.config;

import com.yunsoo.processor.handler.ProductKeyBatchHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/4/2
 * Descriptions:
 */
@Configuration
public class HandlerConfiguration {

    @Bean
    public ProductKeyBatchHandler productKeyBatchHandler() {
        return new ProductKeyBatchHandler();
    }
}
