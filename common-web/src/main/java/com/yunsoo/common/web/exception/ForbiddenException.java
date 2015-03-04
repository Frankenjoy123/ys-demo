package com.yunsoo.common.web.exception;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.error.ErrorResultCode;
import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class ForbiddenException extends APIErrorResultException {

    public ForbiddenException() {
        this(ErrorResultCode.FORBIDDEN);
    }

    public ForbiddenException(int code) {
        this(new ErrorResult(code, "Action forbidden"));
    }

    public ForbiddenException(String actionName, String accountName) {
        this(ErrorResultCode.FORBIDDEN, actionName, accountName);
    }

    public ForbiddenException(int code, String actionName, String accountName) {
        this(new ErrorResult(code, "Action[" + actionName + "] is forbidden for account: " + accountName));
    }

    public ForbiddenException(ErrorResult errorResult) {
        super(HttpStatus.FORBIDDEN, errorResult);
    }
}
