package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.RestErrorResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedException extends RestErrorResultException {

    public UnauthorizedException() {
        this(RestErrorResultCode.UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        this(RestErrorResultCode.UNAUTHORIZED, message);
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
