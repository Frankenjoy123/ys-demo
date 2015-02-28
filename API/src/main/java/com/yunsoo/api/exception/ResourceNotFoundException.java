package com.yunsoo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ErrorResultException {

    public ResourceNotFoundException() {
        super(40400, "Resource not found");
    }

    public ResourceNotFoundException(int code) {
        super(code, "Resource not found");
    }

    public ResourceNotFoundException(String name) {
        super(40400, name + " not found");
    }

    public ResourceNotFoundException(int code, String name) {
        super(code, name + " not found");
    }
}
