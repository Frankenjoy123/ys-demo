package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.RestErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class UnprocessableEntityException extends RestErrorResultException {

    public UnprocessableEntityException() {
        this(RestErrorResultCode.UNPROCESSABLE_ENTITY);
    }

    public UnprocessableEntityException(int code) {
        this(code, "Unprocessable entity");
    }

    public UnprocessableEntityException(String message) {
        this(RestErrorResultCode.UNPROCESSABLE_ENTITY, message);
    }

    public UnprocessableEntityException(int code, String message) {
        this(new ErrorResult(code, message));
    }

    public UnprocessableEntityException(ErrorResult errorResult) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, errorResult);
    }
}
