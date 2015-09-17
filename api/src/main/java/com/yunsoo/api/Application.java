package com.yunsoo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.cache.ElastiCacheAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Filter;

@ComponentScan(basePackages = "com.yunsoo")
@SpringBootApplication(exclude = ElastiCacheAutoConfiguration.class)
public class Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        //set throw exception if no handler mapping
        Object dispatcherServlet = context.getBean("dispatcherServlet");
        if (dispatcherServlet != null && dispatcherServlet instanceof DispatcherServlet) {
            ((DispatcherServlet) dispatcherServlet).setThrowExceptionIfNoHandlerFound(true);
        }
        System.out.println("Run API by Spring Boot. Successfully started...");
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
