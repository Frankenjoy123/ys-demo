package com.yunsoo.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by:   Lijian
 * Created on:   2015/4/1
 * Descriptions: It's added only for avoid bootRepackage issue during building.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        //set throw exception if no handler mapping
        Object dispatcherServlet = context.getBean("dispatcherServlet");
        if (dispatcherServlet != null && dispatcherServlet instanceof DispatcherServlet) {
            ((DispatcherServlet) dispatcherServlet).setThrowExceptionIfNoHandlerFound(true);
        }

        System.out.println("processor started...");
    }

}