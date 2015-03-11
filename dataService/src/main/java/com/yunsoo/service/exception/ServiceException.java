package com.yunsoo.service.exception;

/**
 * Created by:   Lijian
 * Created on:   2015/3/10
 * Descriptions:
 */
public class ServiceException extends RuntimeException {

    private Class<?> source;

    public ServiceException(Class<?> source) {
        this(source, "Unknown Service Exception");
    }

    public ServiceException(Class<?> source, String message) {
        super(source.getName() + ": " + message);
        this.source = source;
    }

    public boolean isThrownFrom(Class<?> source) {
        return source != null && this.source != null && source.isAssignableFrom(this.source);
    }
}
