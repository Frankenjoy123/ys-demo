package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.RestErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/6
 * Descriptions:
 */
public class BadRequestException extends RestErrorResultException {

    public BadRequestException() {
        this(RestErrorResultCode.BAD_REQUEST);
    }

    public BadRequestException(int code) {
        this(code, "Bad Request");
    }

    public BadRequestException(String message) {
        this(RestErrorResultCode.BAD_REQUEST, message);
    }

    public BadRequestException(int code, String message) {
        this(new ErrorResult(code, message));
    }

    public BadRequestException(ErrorResult errorResult) {
        super(HttpStatus.BAD_REQUEST, errorResult);
    }
}
