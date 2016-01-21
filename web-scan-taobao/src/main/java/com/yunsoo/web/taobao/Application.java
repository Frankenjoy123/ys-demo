package com.yunsoo.web.taobao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.yunsoo")
@SpringBootApplication()
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        System.out.println("web-scan-taobao started...");
    }

}
