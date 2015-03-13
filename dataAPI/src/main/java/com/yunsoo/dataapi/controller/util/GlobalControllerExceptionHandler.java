package com.yunsoo.dataapi.controller.util;

import com.yunsoo.common.config.CommonConfig;
import com.yunsoo.common.error.DebugErrorResult;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.error.TraceInfo;
import com.yunsoo.common.web.error.RestErrorResultCode;
import com.yunsoo.common.web.exception.RestErrorResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions:
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @Autowired
    private CommonConfig commonConfig;

    @ExceptionHandler(RestErrorResultException.class)
    @ResponseBody
    public ResponseEntity<ErrorResult> handleRestError(HttpServletRequest req, Exception ex) {
        ErrorResult result;
        HttpStatus status;
        if (ex instanceof RestErrorResultException) {
            RestErrorResultException apiEx = (RestErrorResultException) ex;
            result = apiEx.getErrorResult();
            status = apiEx.getHttpStatus();
        } else {
            result = ErrorResult.UNKNOWN;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        result = appendTraceInfo(result, ex);
        return new ResponseEntity<>(result, status);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResult handleNoHandlerFound(HttpServletRequest req, Exception ex) {
        ErrorResult result = new ErrorResult(RestErrorResultCode.NOT_FOUND, "no handler found");
        return appendTraceInfo(result, ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult handleServerError(HttpServletRequest req, Exception ex) {
        ErrorResult result = ErrorResult.UNKNOWN;
        return appendTraceInfo(result, ex);
    }

    private ErrorResult appendTraceInfo(ErrorResult result, Exception ex) {
        if (commonConfig.isDebugEnabled()) {
            result = new DebugErrorResult(result, new TraceInfo(ex));
        }
        return result;
    }
}