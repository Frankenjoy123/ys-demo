
package com.yunsoo.api.config;

import com.yunsoo.api.data.DataAPIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
@Configuration
@ComponentScan(basePackages = {"com.yunsoo.api.config"})
public class DataAPIConfiguration {

    @Bean
    public static PropertyPlaceholderConfigurer yunsooProperties() throws IOException {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocations(new ClassPathResource[]{new ClassPathResource("yunsoo.properties")});
        //ppc.setIgnoreUnresolvablePlaceholders(true);
        return ppc;
    }

    @Value("${yunsoo.dataapi.baseurl}")
    private String dataAPIBaseURL;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DataAPIClient dataAPIClient() {
        if (!dataAPIBaseURL.endsWith("/")) {
            dataAPIBaseURL += "/";
        }
        return new DataAPIClient(dataAPIBaseURL);
    }

}