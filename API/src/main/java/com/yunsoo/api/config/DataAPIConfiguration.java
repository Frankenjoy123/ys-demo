
package com.yunsoo.api.config;

import com.yunsoo.api.data.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
@Configuration
@ComponentScan(basePackageClasses = YunsooConfiguration.class)
public class DataAPIConfiguration {

    @Autowired
    private String dataAPIBaseURL;

    @Bean
    public RestClient dataAPIClient() {
        if (!dataAPIBaseURL.endsWith("/")) {
            dataAPIBaseURL += "/";
        }
        return new RestClient(dataAPIBaseURL);
    }

}