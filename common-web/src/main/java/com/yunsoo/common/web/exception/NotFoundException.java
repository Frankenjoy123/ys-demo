package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.RestErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class NotFoundException extends RestErrorResultException {

    public NotFoundException() {
        this(RestErrorResultCode.NOT_FOUND);
    }

    public NotFoundException(int code) {
        this(code, "Resource not found");
    }

    public NotFoundException(String message) {
        this(RestErrorResultCode.NOT_FOUND, message);
    }

    public NotFoundException(int code, String message) {
        this(new ErrorResult(code, message));
    }

    public NotFoundException(ErrorResult errorResult) {
        super(HttpStatus.NOT_FOUND, errorResult);
    }
}
