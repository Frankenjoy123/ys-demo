package com.yunsoo.data.api;

import com.yunsoo.common.util.UuidUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.HashSet;

@ComponentScan(basePackages = "com.yunsoo")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);

        //set throw exception if no handler mapping
        Object dispatcherServlet = context.getBean("dispatcherServlet");
        if (dispatcherServlet != null && dispatcherServlet instanceof DispatcherServlet) {
            ((DispatcherServlet) dispatcherServlet).setThrowExceptionIfNoHandlerFound(true);
        }

//        HashSet<String> set = new HashSet<>();
//        for(int i=0; i< 1000000; i++) {
//            String util = UuidUtil.get().toString();
//            if(set.contains(util)){
//                System.out.println("Wrong!");
//            }else {
//                set.add(util);
//            }
//        }

        System.out.println("Run DataAPI by Spring Boot. Successfully started...");
    }
}
