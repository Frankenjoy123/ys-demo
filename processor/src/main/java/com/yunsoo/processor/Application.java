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

        try {
            Method method = SqsController.class.getDeclaredMethod("receiveMessage", ProductKeyBatchMassage.class, String.class);
            System.out.println(method.getName());
            MessageMapping mapping = method.getAnnotation(MessageMapping.class);
            String[] queueNames = mapping.value();
            for (int i = 0; i < queueNames.length; i++) {
                queueNames[i] = "dev-" + queueNames[i];
            }
            System.out.println(Arrays.asList(queueNames));


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


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