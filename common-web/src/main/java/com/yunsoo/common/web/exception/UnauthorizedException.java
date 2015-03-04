package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.ErrorResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedException extends APIErrorResultException {

    public UnauthorizedException() {
        this(ErrorResultCode.UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        this(ErrorResultCode.UNAUTHORIZED, message);
    }

    public UnauthorizedException(int code) {
        this(code, "Unauthorized");
    }

    public UnauthorizedException(int code, String message) {
        this(new ErrorResult(code, message));
    }

    public UnauthorizedException(ErrorResult errorResult) {
        super(HttpStatus.UNAUTHORIZED, errorResult);
    }

}
