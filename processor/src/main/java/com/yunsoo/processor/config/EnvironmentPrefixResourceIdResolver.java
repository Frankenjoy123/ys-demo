package com.yunsoo.processor.config;

import org.springframework.cloud.aws.core.env.ResourceIdResolver;

/**
 * Created by  : Lijian
 * Created on  : 2015/4/26
 * Descriptions:
 */
public class EnvironmentPrefixResourceIdResolver implements ResourceIdResolver {

    private String environment;

    public EnvironmentPrefixResourceIdResolver(String environment) {
        this.environment = environment;
    }

    @Override
    public String resolveToPhysicalResourceId(String logicalResourceId) {
        String physicalResourceId = logicalResourceId;
        if (environment != null && environment.length() > 0) {
            physicalResourceId = environment + "-" + logicalResourceId;
        }
        return physicalResourceId;
    }
}
