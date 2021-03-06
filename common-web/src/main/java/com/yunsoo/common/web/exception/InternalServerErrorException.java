package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.RestErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class InternalServerErrorException extends RestErrorResultException {

    public InternalServerErrorException() {
        this(RestErrorResultCode.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(int code) {
        this(code, "Internal server error");
    }

    public InternalServerErrorException(String message) {
        this(RestErrorResultCode.INTERNAL_SERVER_ERROR, message);
    }

    public InternalServerErrorException(int code, String message) {
        this(new ErrorResult(code, message));
    }

    public InternalServerErrorException(ErrorResult errorResult) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorResult);
    }
}
