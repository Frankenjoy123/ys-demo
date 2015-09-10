package com.yunsoo.api.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.context.annotation.OnAwsCloudEnvironmentCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by:   yan
 * Created on:   7/30/2015
 * Descriptions:
 */
public class NotOnAwsCloudEnvironmentCondition implements ConfigurationCondition {

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.PARSE_CONFIGURATION;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String enableElastiCache = context.getEnvironment().getProperty("yunsoo.elasticache");
        if(enableElastiCache == null || !Boolean.valueOf(enableElastiCache))
            return true;

        OnAwsCloudEnvironmentCondition condition = new OnAwsCloudEnvironmentCondition();
        return !condition.matches(context, metadata);
    }
}
