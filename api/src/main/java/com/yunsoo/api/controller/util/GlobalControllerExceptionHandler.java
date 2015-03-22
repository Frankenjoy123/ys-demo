package com.yunsoo.api.controller.util;

import com.yunsoo.common.config.CommonConfig;
import com.yunsoo.common.error.DebugErrorResult;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.error.TraceInfo;
import com.yunsoo.common.web.error.RestErrorResultCode;
import com.yunsoo.common.web.exception.RestErrorResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions:
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @Autowired
    private CommonConfig commonConfig;

    //business
    @ExceptionHandler(RestErrorResultException.class)
    @ResponseBody
    public ResponseEntity<ErrorResult> handleRestError(HttpServletRequest req, RestErrorResultException ex) {
        ErrorResult result = ex.getErrorResult();
        HttpStatus status = ex.getHttpStatus();
        result = appendTraceInfo(result, ex);
        return new ResponseEntity<>(result, status);
    }

    //400
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleMissingRequestParameterException(HttpServletRequest req, Exception ex) {
        String message;
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException mEx = (MethodArgumentNotValidException) ex;
            message = ObjectUtils.nullSafeToString(
                    mEx.getBindingResult().getFieldErrors()
                            .stream()
                            .map(e -> e.getField() + ": " + e.getDefaultMessage())
                            .collect(Collectors.toList()));
        } else {
            message = ex.getMessage();
        }
        ErrorResult result = new ErrorResult(RestErrorResultCode.BAD_REQUEST, message);
        return appendTraceInfo(result, ex);
    }

    //404
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResult handleNoHandlerFound(HttpServletRequest req, Exception ex) {
        ErrorResult result = new ErrorResult(RestErrorResultCode.NOT_FOUND, "no handler found");
        return appendTraceInfo(result, ex);
    }

    //500
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