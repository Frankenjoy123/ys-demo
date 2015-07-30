package com.yunsoo.api.cache;

import org.springframework.cloud.aws.context.annotation.OnAwsCloudEnvironmentCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by yan on 7/30/2015.
 */
public class NotOnAwsCloudEnvironmentCondition implements ConfigurationCondition {

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.PARSE_CONFIGURATION;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        OnAwsCloudEnvironmentCondition condition = new OnAwsCloudEnvironmentCondition();
        return !condition.matches(context, metadata);
    }
}
