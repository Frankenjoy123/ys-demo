package com.yunsoo.common.exception;

import com.yunsoo.common.error.ErrorResult;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class ErrorResultException extends RuntimeException {

    private ErrorResult errorResult;

    public ErrorResultException() {
        this(ErrorResult.UNKNOWN);
    }


    public ErrorResultException(ErrorResult errorResult) {
        super(errorResult.toString());
        this.errorResult = errorResult;
    }

    public ErrorResult getErrorResult() {
        return errorResult;
    }

    public ErrorResultException withInnerException(Exception exception) {
        if (getCause() == null && exception != this) {
            initCause(exception);
        }
        return this;
    }
}
