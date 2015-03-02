package com.yunsoo.api.exception;

import com.yunsoo.api.dto.basic.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions:
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ErrorResult handleBadRequest(HttpServletRequest req, Exception ex) {
        if (ex instanceof ErrorResultException) {
            return ((ErrorResultException) ex).toErrorResult();
        } else {
            return new ErrorResult(40400, "Resource not found");
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResult handleServerError(HttpServletRequest req, Exception ex) {
        if (ex instanceof ErrorResultException) {
            return ((ErrorResultException) ex).toErrorResult();
        }

        return new ErrorResult(50000, ex.toString());
        //return new ErrorResult(50000, "Internal Server Error");
    }

}
