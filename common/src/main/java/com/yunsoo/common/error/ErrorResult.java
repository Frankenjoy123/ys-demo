package com.yunsoo.common.error;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class ErrorResult {

    public final static ErrorResult UNKNOWN = new ErrorResult(ErrorResultCode.UNKNOWN, "Unknown Error");


    private int code;
    private String message;

    public ErrorResult() {
    }

    public ErrorResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + code + "] " + message;
    }

}