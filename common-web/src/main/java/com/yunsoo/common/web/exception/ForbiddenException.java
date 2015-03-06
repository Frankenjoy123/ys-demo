package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.RestErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class ForbiddenException extends RestErrorResultException {

    public ForbiddenException() {
        this(RestErrorResultCode.FORBIDDEN);
    }

    public ForbiddenException(int code) {
        this(new ErrorResult(code, "Action forbidden"));
    }

    public ForbiddenException(String actionName, String accountName) {
        this(RestErrorResultCode.FORBIDDEN, actionName, accountName);
    }

    public ForbiddenException(int code, String actionName, String accountName) {
        this(new ErrorResult(code, "Action[" + actionName + "] is forbidden for account: " + accountName));
    }

    public ForbiddenException(ErrorResult errorResult) {
        super(HttpStatus.FORBIDDEN, errorResult);
    }
}
