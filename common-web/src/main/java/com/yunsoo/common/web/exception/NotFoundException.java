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
        this(code, "Resource");
    }

    public NotFoundException(String resourceName) {
        this(RestErrorResultCode.NOT_FOUND, resourceName);
    }

    public NotFoundException(int code, String resourceName) {
        this(new ErrorResult(code, resourceName + " not found"));
    }

    public NotFoundException(ErrorResult errorResult) {
        super(HttpStatus.NOT_FOUND, errorResult);
    }
}
