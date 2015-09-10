package com.yunsoo.api.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.context.annotation.OnAwsCloudEnvironmentCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by yan on 9/10/2015.
 */
public class EnableElastiCacheCondition implements ConfigurationCondition {

    @Override
    public ConfigurationCondition.ConfigurationPhase getConfigurationPhase() {
        return ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION;
    }

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String enableElastiCache = context.getEnvironment().getProperty("yunsoo.elasticache");
        if(enableElastiCache != null &&  Boolean.valueOf(enableElastiCache)) {
            OnAwsCloudEnvironmentCondition condition = new OnAwsCloudEnvironmentCondition();
            return condition.matches(context, metadata);
        }

        return false;
    }
}
