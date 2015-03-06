package com.yunsoo.common.exception;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
public class ConfigurationErrorException extends Exception {
    private String configurationName;

    public ConfigurationErrorException(String configurationName) {
        super(configurationName + " not well configured");
    }

    public ConfigurationErrorException(String configurationName, Exception exception) {
        super(configurationName + " not well configured", exception);
    }

    public String getConfigurationName() {
        return configurationName;
    }
}
