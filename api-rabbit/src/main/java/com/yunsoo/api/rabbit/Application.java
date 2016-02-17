package com.yunsoo.api.rabbit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.cache.ElastiCacheAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;

@ComponentScan(basePackages = "com.yunsoo")
@SpringBootApplication(exclude = ElastiCacheAutoConfiguration.class)
public class Application {

    private static Log log = LogFactory.getLog(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        log.info("api-rabbit started...");
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
