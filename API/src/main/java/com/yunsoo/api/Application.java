package com.yunsoo.api;

import com.yunsoo.api.security.StatelessAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Filter;

//@ComponentScan
//@EnableAutoConfiguration
//@Configuration
@SpringBootApplication
@Import(StatelessAuthenticationSecurityConfig.class)
public class Application {

    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//        TPage page = restTemplate.getForObject("http://graph.facebook.com/pivotalsoftware", TPage.class);
//        System.out.println("Name:    " + page.getName());
//        System.out.println("About:   " + page.getAbout());
//        System.out.println("Phone:   " + page.getPhone());
//        System.out.println("Website: " + page.getWebsite());

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

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
