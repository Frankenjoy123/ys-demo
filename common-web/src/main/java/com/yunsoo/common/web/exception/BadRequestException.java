package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.APIErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/6
 * Descriptions:
 */
public class BadRequestException extends APIErrorResultException {

    public BadRequestException() {
        this(APIErrorResultCode.FORBIDDEN);
    }

    public BadRequestException(int code) {
        this(code, "Bad Request");
    }

    public BadRequestException(String message) {
        this(APIErrorResultCode.BAD_REQUEST, message);
    }

    public BadRequestException(int code, String message) {
        this(new ErrorResult(code, message));
    }

    public BadRequestException(ErrorResult errorResult) {
        super(HttpStatus.BAD_REQUEST, errorResult);
    }
}
