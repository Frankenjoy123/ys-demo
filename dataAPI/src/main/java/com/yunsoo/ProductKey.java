package com.yunsoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

public class ProductKey {
    private String key;
    private String productId;

    public ProductKey(String key, String productId) {
        this.key = key;
        this.productId = productId;
    }

    public String getKey() {
        return key;
    }

    public String getProductId() {
        return productId;
    }

}
