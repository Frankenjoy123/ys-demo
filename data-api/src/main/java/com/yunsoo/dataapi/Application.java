package com.yunsoo.dataapi;

import com.yunsoo.config.DBSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Arrays;

@ComponentScan(basePackages = {"com.yunsoo"})
@EnableAutoConfiguration
@Configuration
public class Application {


    @Autowired
    public DBSetting dbSetting;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);

        //set throw exception if no handler mapping
        Object dispatcherServlet = context.getBean("dispatcherServlet");
        if (dispatcherServlet != null && dispatcherServlet instanceof DispatcherServlet) {
            ((DispatcherServlet) dispatcherServlet).setThrowExceptionIfNoHandlerFound(true);
        }

        System.out.println("Run DataAPI by Spring Boot. Successfully started...");

    }
}
