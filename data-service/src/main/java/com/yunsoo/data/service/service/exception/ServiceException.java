package com.yunsoo.data.service.service.exception;

/**
 * Created by:   Lijian
 * Created on:   2015/3/10
 * Descriptions:
 */
public class ServiceException extends RuntimeException {

    private Type type;

    public ServiceException(Type type) {
        this(type, type.getDefaultMessage());
    }

    public ServiceException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public static ServiceException notFound(String message) {
        return new ServiceException(Type.NotFound, message);
    }

    public enum Type {
        NotFound("resource not found"),
        InternalServerError("internal server error");

        private String defaultMessage;

        public String getDefaultMessage() {
            return defaultMessage;
        }

        Type(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }
    }

}
