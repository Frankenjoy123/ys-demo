package com.yunsoo.api;

import com.yunsoo.api.thread.OperationThread;
import com.yunsoo.api.util.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


import java.util.Properties;

@ComponentScan(basePackages = "com.yunsoo")
@SpringBootApplication
public class Application {

    private static Log log = LogFactory.getLog(Application.class);

    public static void main(String[] args) {


        ConfigurableApplicationContext app = SpringApplication.run(Application.class, args);
        //app.getClassLoader();
        log.info(String.format("%s started...", app.getId()));

    }




}
