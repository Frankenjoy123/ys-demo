package com.yunsoo.common.error;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
public class TraceInfo {

    private String message;
    private StackTraceElement[] stackTrace;
    private TraceInfo innerTraceInfo;

    public TraceInfo(String message) {
        this.message = message;
    }

    public TraceInfo(Exception exception) {
        if (exception != null) {
            this.message = exception.toString();
            this.stackTrace = exception.getStackTrace();
        }
    }

    public TraceInfo(String message, Exception exception) {
        this.message = message;
        if (exception != null) {
            this.stackTrace = exception.getStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public TraceInfo getInnerTraceInfo() {
        return innerTraceInfo == this ? null : innerTraceInfo;
    }
}
