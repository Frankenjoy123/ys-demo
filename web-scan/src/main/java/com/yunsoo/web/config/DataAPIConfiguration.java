
package com.yunsoo.web.config;

import com.yunsoo.web.APIClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
@Configuration
public class DataAPIConfiguration {

    @Bean
    public APIClient apiClient() {
        String dataAPIBaseURL = "http://localhost/api-rabbit";
        if (!dataAPIBaseURL.endsWith("/")) {
            dataAPIBaseURL += "/";
        }
        return new APIClient(dataAPIBaseURL);
    }

}