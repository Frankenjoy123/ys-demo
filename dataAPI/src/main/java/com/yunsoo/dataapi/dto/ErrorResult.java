package com.yunsoo.dataapi.dto;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions:
 */
public class ErrorResult {

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

}
