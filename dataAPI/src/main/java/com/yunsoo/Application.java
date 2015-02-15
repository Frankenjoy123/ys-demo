package com.yunsoo;

import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import java.util.Arrays;

@ComponentScan
@EnableAutoConfiguration
@Configuration
public class Application {

    public static void main(String[] args) {
//        ApplicationContext context =
//                new AnnotationConfigApplicationContext(Application.class);

        //ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml.bc");
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        ctx.getBean("sessionFactory");
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }


}
