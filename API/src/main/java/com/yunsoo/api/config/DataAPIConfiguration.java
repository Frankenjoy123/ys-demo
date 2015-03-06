
package com.yunsoo.api.config;

import com.yunsoo.api.data.DataAPIClient;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new DataAPIClient(dataAPIBaseURL);
    }

}