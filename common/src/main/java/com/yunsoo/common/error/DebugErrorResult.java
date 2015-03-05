package com.yunsoo.common.error;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
public class DebugErrorResult extends ErrorResult {

    private TraceInfo traceInfo;

    public DebugErrorResult() {
    }

    public DebugErrorResult(ErrorResult errorResult, TraceInfo traceInfo) {
        super((errorResult == null ? ErrorResult.UNKNOWN : errorResult).getCode(),
                (errorResult == null ? ErrorResult.UNKNOWN : errorResult).getMessage());
        this.traceInfo = traceInfo;
    }

    public TraceInfo getTraceInfo() {
        return traceInfo;
    }
}
