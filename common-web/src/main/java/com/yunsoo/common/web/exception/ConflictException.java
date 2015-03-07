package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.RestErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/6
 * Descriptions:
 */
public class ConflictException extends RestErrorResultException {

    public ConflictException() {
        this(RestErrorResultCode.CONFLICT);
    }

    public ConflictException(int code) {
        this(code, "Conflict");
    }

    public ConflictException(String message) {
        this(RestErrorResultCode.CONFLICT, message);
    }

    public ConflictException(int code, String message) {
        this(new ErrorResult(code, message));
    }

    public ConflictException(ErrorResult errorResult) {
        super(HttpStatus.CONFLICT, errorResult);
    }
}
