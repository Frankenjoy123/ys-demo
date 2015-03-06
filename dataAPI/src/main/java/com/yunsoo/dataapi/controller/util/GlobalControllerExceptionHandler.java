package com.yunsoo.dataapi.controller.util;

import com.yunsoo.common.config.CommonConfig;
import com.yunsoo.common.error.DebugErrorResult;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.error.TraceInfo;
import com.yunsoo.common.web.exception.APIErrorResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ExceptionHandler(APIErrorResultException.class)
    @ResponseBody
    public ResponseEntity<ErrorResult> handleBadRequest(HttpServletRequest req, Exception ex) {
        ErrorResult result;
        HttpStatus status;
        if (ex instanceof APIErrorResultException) {
            APIErrorResultException apiEx = (APIErrorResultException) ex;
            result = apiEx.getErrorResult();
            status = apiEx.getHttpStatus();
        } else {
            result = ErrorResult.UNKNOWN;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        if (commonConfig.isDebugEnabled()) {
            result = new DebugErrorResult(result, new TraceInfo(ex));
        }
        return new ResponseEntity<>(result, status);

    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResult handleServerError(HttpServletRequest req, Exception ex) {
        ErrorResult result = ErrorResult.UNKNOWN;
        if (commonConfig.isDebugEnabled()) {
            result = new DebugErrorResult(result, new TraceInfo(ex));
        }
        return result;
    }

}
