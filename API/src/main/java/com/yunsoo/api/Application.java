package com.yunsoo.api;

import com.yunsoo.api.object.Page;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

@ComponentScan
@EnableAutoConfiguration
@Configuration
public class Application {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        Page page = restTemplate.getForObject("http://graph.facebook.com/pivotalsoftware", Page.class);
        System.out.println("Name:    " + page.getName());
        System.out.println("About:   " + page.getAbout());
        System.out.println("Phone:   " + page.getPhone());
        System.out.println("Website: " + page.getWebsite());

        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        System.out.println("Run API by Spring Boot:");
    }


}
