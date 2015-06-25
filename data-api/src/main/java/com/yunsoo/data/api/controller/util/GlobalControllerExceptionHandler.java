package com.yunsoo.data.api.controller.util;

import com.yunsoo.common.error.DebugErrorResult;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.error.TraceInfo;
import com.yunsoo.common.web.error.RestErrorResultCode;
import com.yunsoo.common.web.exception.RestErrorResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
    private static final String FROM = "data-api";

    @Value("${yunsoo.debug}")
    private Boolean debug;

    //business
    @ExceptionHandler(RestErrorResultException.class)
    @ResponseBody
    public ResponseEntity<ErrorResult> handleRestError(HttpServletRequest req, RestErrorResultException ex) {
        HttpStatus status = ex.getHttpStatus();
        ErrorResult result = ex.getErrorResult();
        LOGGER.info("[from: {}, status: {}, message: {}]", FROM, status, result);
        return new ResponseEntity<>(appendTraceInfo(result, ex), status);
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
        LOGGER.warn("[from: {}, status: 400, message: {}]", FROM, message);
        return appendTraceInfo(result, ex);
    }

    //404
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResult handleNoHandlerFound(HttpServletRequest req, Exception ex) {
        ErrorResult result = new ErrorResult(RestErrorResultCode.NOT_FOUND, "no handler found");
        LOGGER.info("[from: {}, status: 404, message: {}]", FROM, ex.getMessage());
        return appendTraceInfo(result, ex);
    }

    //405
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResult handleMethodNotSupported(HttpServletRequest req, Exception ex) {
        ErrorResult result = new ErrorResult(RestErrorResultCode.METHOD_NOT_ALLOWED, "method not allowed");
        LOGGER.info("[from: {}, status: 405, message: {}]", FROM, ex.getMessage());
        return appendTraceInfo(result, ex);
    }

    //500
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult handleServerError(HttpServletRequest req, Exception ex) {
        ErrorResult result = new ErrorResult(RestErrorResultCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        LOGGER.error("[from: " + FROM + ", status: 500, message: " + ex.getMessage() + "]", ex);
        return appendTraceInfo(result, ex);
    }

    private ErrorResult appendTraceInfo(ErrorResult result, Exception ex) {
        if (debug != null && debug) {
            result = new DebugErrorResult(result, new TraceInfo(ex));
        }
        return result;
    }
}
