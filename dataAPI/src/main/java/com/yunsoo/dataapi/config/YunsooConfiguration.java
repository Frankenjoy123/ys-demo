package com.yunsoo.dataapi.config;

import com.yunsoo.common.config.CommonConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
@Configuration
public class YunsooConfiguration {

    @Value("${yunsoo.debug}")
    private String debug;

    @Bean
    public static PropertyPlaceholderConfigurer yunsooProperties() throws IOException {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocations(new ClassPathResource[]{new ClassPathResource("yunsoo.properties")});
        ppc.setIgnoreUnresolvablePlaceholders(true);
        return ppc;
    }

    @Bean
    public CommonConfig commonConfig() {
        CommonConfig config = new CommonConfig();
        config.setDebugEnabled(Boolean.parseBoolean(debug));
        return config;
    }

}
