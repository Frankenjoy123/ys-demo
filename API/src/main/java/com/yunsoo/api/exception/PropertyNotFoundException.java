package com.yunsoo.api.exception;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class PropertyNotFoundException extends Exception {

    private String propertyName;

    public String getPropertyName() {
        return this.propertyName;
    }

    public PropertyNotFoundException(String propertyName) {
        this.propertyName = propertyName;
    }

    public PropertyNotFoundException(String propertyName, String message) {
        super(message);
        this.propertyName = propertyName;
    }
}
