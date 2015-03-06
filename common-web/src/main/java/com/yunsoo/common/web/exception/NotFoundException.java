package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.APIErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class NotFoundException extends APIErrorResultException {

    public NotFoundException() {
        this(APIErrorResultCode.NOT_FOUND);
    }

    public NotFoundException(int code) {
        this(code, "Resource");
    }

    public NotFoundException(String resourceName) {
        this(APIErrorResultCode.NOT_FOUND, resourceName);
    }

    public NotFoundException(int code, String resourceName) {
        this(new ErrorResult(code, resourceName + " not found"));
    }

    public NotFoundException(ErrorResult errorResult) {
        super(HttpStatus.NOT_FOUND, errorResult);
    }
}
