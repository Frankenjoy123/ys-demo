package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.ErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class UnprocessableEntityException extends APIErrorResultException {

    public UnprocessableEntityException() {
        this(ErrorResultCode.UNPROCESSABLE_ENTITY);
    }

    public UnprocessableEntityException(int code) {
        this(code, "Unprocessable entity");
    }

    public UnprocessableEntityException(String message) {
        this(ErrorResultCode.UNPROCESSABLE_ENTITY, message);
    }

    public UnprocessableEntityException(int code, String message) {
        this(new ErrorResult(code, message));
    }

    public UnprocessableEntityException(ErrorResult errorResult) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, errorResult);
    }
}
