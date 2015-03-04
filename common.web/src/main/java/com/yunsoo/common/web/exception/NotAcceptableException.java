package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.ErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class NotAcceptableException extends APIErrorResultException {

    public NotAcceptableException() {
        this(ErrorResultCode.NOT_ACCEPTABLE);
    }

    public NotAcceptableException(int code) {
        this(code, "Not Acceptable");
    }

    public NotAcceptableException(String message) {
        this(ErrorResultCode.NOT_ACCEPTABLE, message);
    }

    public NotAcceptableException(int code, String message) {
        this(new ErrorResult(code, message));
    }

    public NotAcceptableException(ErrorResult errorResult) {
        super(HttpStatus.NOT_ACCEPTABLE, errorResult);
    }
}