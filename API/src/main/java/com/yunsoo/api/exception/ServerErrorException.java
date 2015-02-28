package com.yunsoo.api.exception;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions:
 */
public class ServerErrorException extends ErrorResultException {

    public ServerErrorException() {
        super(50000, "Internal server error");
    }

    public ServerErrorException(int code) {
        super(code, "Internal server error");
    }

    public ServerErrorException(String message) {
        super(50000, message);
    }

    public ServerErrorException(int code, String message) {
        super(code, message);
    }
}
