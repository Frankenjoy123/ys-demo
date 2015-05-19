package com.yunsoo.common.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
        return "{code: " + code + ", message: " + message + "}";
    }

}
