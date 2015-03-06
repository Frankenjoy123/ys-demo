package com.yunsoo.api.config;

import com.yunsoo.common.error.DebugConfig;
import com.yunsoo.common.exception.ConfigurationErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URI;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
@Configuration
public class YunsooConfiguration {

    @Value("${yunsoo.debug}")
    private String debug;

    @Value("${yunsoo.dataapi.baseurl}")
    private String dataAPIBaseURL;

    @Bean
    public static PropertyPlaceholderConfigurer yunsooProperties() throws IOException {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocations(new ClassPathResource[]{new ClassPathResource("yunsoo.properties")});
        ppc.setIgnoreUnresolvablePlaceholders(true);
        return ppc;
    }

    @Bean
    public DebugConfig debugConfig() {
        return new DebugConfig(Boolean.parseBoolean(debug));
    }

    @Bean
    public String dataAPIBaseURL() throws ConfigurationErrorException {
        if (dataAPIBaseURL == null || dataAPIBaseURL.equals("${yunsoo.dataapi.baseurl}")) {
            throw new ConfigurationErrorException("yunsoo.dataapi.baseurl");
        }
        return dataAPIBaseURL;
    }

}
