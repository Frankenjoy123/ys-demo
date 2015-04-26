package com.yunsoo.processor;

import com.yunsoo.processor.controller.SqsController;
import com.yunsoo.processor.message.ProductKeyBatchMassage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.servlet.DispatcherServlet;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by:   Lijian
 * Created on:   2015/4/1
 * Descriptions:
 */

@ComponentScan(basePackages = {"com.yunsoo"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        //set throw exception if no handler mapping
        Object dispatcherServlet = context.getBean("dispatcherServlet");
        if (dispatcherServlet != null && dispatcherServlet instanceof DispatcherServlet) {
            ((DispatcherServlet) dispatcherServlet).setThrowExceptionIfNoHandlerFound(true);
        }

        System.out.println(Arrays.asList(context.getBeanDefinitionNames()));


        System.out.println("processor started...");
    }

}