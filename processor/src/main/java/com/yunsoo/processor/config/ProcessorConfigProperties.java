package com.yunsoo.processor.config;

/**
 * Created by:   Lijian
 * Created on:   2016-04-22
 * Descriptions:
 */
public class ProcessorConfigProperties {

    private String name;

    private String environment;

    private boolean debug;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

}
