
package com.yunsoo.api.config;

import com.yunsoo.api.data.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

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
    public RestClient dataAPIClient() {
        if (!dataAPIBaseURL.endsWith("/")) {
            dataAPIBaseURL += "/";
        }
        return new RestClient(dataAPIBaseURL);
    }

}