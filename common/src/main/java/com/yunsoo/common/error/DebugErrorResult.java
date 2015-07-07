package com.yunsoo.common.error;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Lijian
 * Created on:   2015/3/5
 * Descriptions:
 */
public class DebugErrorResult extends ErrorResult {

    @JsonProperty("trace_info")
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
