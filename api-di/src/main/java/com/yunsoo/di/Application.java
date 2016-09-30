package com.yunsoo.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.yunsoo.di")
@SpringBootApplication
public class Application {

    private static Log log = LogFactory.getLog(Application.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(Application.class, args);

        log.info(String.format("%s started...", app.getId()));
    }

}
