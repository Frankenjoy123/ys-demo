package com.yunsoo.api.controller;

import com.yunsoo.api.exception.ResourceNotFoundException;
import com.yunsoo.api.exception.ServerErrorException;
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

    @RequestMapping(value = "/ok")
    @ResponseStatus(HttpStatus.OK)
    public String ok() {
        return "200 OK";
    }

    @RequestMapping(value = "/created")
    @ResponseStatus(HttpStatus.CREATED)
    public void created() {
    }

    @RequestMapping(value = "/accepted")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void accepted() {
    }

    @RequestMapping(value = "/nocontent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void noContent() {
    }

    @RequestMapping(value = "/error/{code}")
    public void throwError(@PathVariable(value = "code") int code) {
        HttpStatus status = HttpStatus.valueOf(code);
        if (status.is4xxClientError()) {
            throw new ResourceNotFoundException();
        } else if (status.is5xxServerError()) {
            throw new ServerErrorException();
        }
    }
}
