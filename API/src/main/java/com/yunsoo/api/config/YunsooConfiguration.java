package com.yunsoo.api.config;

import com.yunsoo.common.config.CommonConfig;
import com.yunsoo.common.exception.ConfigurationErrorException;
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

    @Value("${yunsoo.dataapi.baseurl}")
    private String dataAPIBaseURL;

    @Value("${yunsoo.productbase.picture.basepath}")
    private String productBasePicURL;

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

    @Bean
    public String dataAPIBaseURL() {
        if (dataAPIBaseURL == null || dataAPIBaseURL.equals("${yunsoo.dataapi.baseurl}")) {
            throw new ConfigurationErrorException("yunsoo.dataapi.baseurl");
        }
        return dataAPIBaseURL;
    }

    @Bean
    public String productBasePicURL() {
        if (productBasePicURL == null || productBasePicURL.equals("${yunsoo.productbase.picture.basepath}")) {
            throw new ConfigurationErrorException("yunsoo.productbase.picture.basepath");
        }
        return productBasePicURL;
    }
}
