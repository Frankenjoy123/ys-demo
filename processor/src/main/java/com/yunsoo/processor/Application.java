package com.yunsoo.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by:   Lijian
 * Created on:   2015/4/1
 * Descriptions:
 */

@ComponentScan(basePackages = {"com.yunsoo"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        System.out.println("processor started...");
    }

}