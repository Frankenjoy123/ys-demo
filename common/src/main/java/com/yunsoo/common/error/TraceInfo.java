package com.yunsoo.common.error;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
public class TraceInfo {

    @JsonProperty("message")
    private String message;

    @JsonProperty("stack_trace")
    private StackTraceElement[] stackTrace;

    @JsonProperty("inner_trace_info")
    private TraceInfo innerTraceInfo;

    public TraceInfo() {
    }

    public TraceInfo(String message) {
        this.message = message;
    }

    public TraceInfo(Throwable exception) {
        if (exception != null) {
            this.message = exception.toString();
            this.stackTrace = exception.getStackTrace();
        }
    }

    public TraceInfo(String message, Throwable exception) {
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
