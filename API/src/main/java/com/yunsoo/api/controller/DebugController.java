package com.yunsoo.api.controller;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.error.ErrorResultCode;
import com.yunsoo.common.web.exception.RestErrorResultException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions: For developer debug use only.
 */
@RestController
@RequestMapping(value = "/debug")
public class DebugController {

    @RequestMapping(value = "ok")
    @ResponseStatus(HttpStatus.OK)
    public String ok() {
        return "200 OK";
    }

    @RequestMapping(value = "created")
    @ResponseStatus(HttpStatus.CREATED)
    public void created() {
    }

    @RequestMapping(value = "accepted")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void accepted() {
    }

    @RequestMapping(value = "nocontent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void noContent() {
    }

    @RequestMapping(value = "error/{code}")
    public void throwError(@PathVariable(value = "code") int code) throws RestErrorResultException {
        HttpStatus status;
        RestErrorResultException apiEx;
        try {
            status = HttpStatus.valueOf(code);
        } catch (IllegalArgumentException ex) {
            throw new NotFoundException("Error");
        }
        if (status.is4xxClientError() || status.is5xxServerError()) {
            throw new RestErrorResultException(status, new ErrorResult(ErrorResultCode.DEBUG, "Debug"));
        }
        throw new NotFoundException("Error");
    }

    @RequestMapping(value = "error")
    public void throwError() throws Exception {
        throw new Exception("Error");
    }
}
