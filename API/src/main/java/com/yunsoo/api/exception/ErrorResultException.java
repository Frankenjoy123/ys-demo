package com.yunsoo.api.exception;

import com.yunsoo.api.dto.basic.ErrorResult;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions:
 */
public class ErrorResultException extends RuntimeException {
    private int code;

    public ErrorResultException() {
        super("Unknown Exception");
        this.code = 0;
    }

    public ErrorResultException(int code) {
        super("Unknown Exception");
        this.code = code;
    }

    public ErrorResultException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorResult toErrorResult() {
        return new ErrorResult(code, getMessage());
    }
}
