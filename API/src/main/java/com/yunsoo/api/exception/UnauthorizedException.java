package com.yunsoo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Zhe on 2015/2/27.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedException extends ErrorResultException {

    public UnauthorizedException() {
        super(40300, "Unauthorized action");
    }

    public UnauthorizedException(int code) {
        super(code, "Unauthorized action");
    }

    public UnauthorizedException(String accountName) {
        super(40300, "Unauthorized action for account: " + accountName);
    }

    public UnauthorizedException(int code, String accountName) {
        super(code, "Unauthorized action for account: " + accountName);
    }
}
