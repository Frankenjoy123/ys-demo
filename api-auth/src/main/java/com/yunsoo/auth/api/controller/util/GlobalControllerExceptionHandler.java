package com.yunsoo.auth.api.controller.util;

import com.yunsoo.common.error.DebugErrorResult;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.error.TraceInfo;
import com.yunsoo.common.web.error.RestErrorResultCode;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.RestErrorResultException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/2/28
 * Descriptions:
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final String FROM = "api-auth";

    private Log log = LogFactory.getLog(this.getClass());

    @Value("${yunsoo.debug}")
    private Boolean debug;

    //business
    @ExceptionHandler(RestErrorResultException.class)
    public ResponseEntity<ErrorResult> handleRestError(HttpServletRequest req, RestErrorResultException ex) {
        HttpStatus status = ex.getHttpStatus();
        ErrorResult result = ex.getErrorResult();
        log.info(String.format("[from: %s, status: %s, message: %s]", FROM, status, result));
        return new ResponseEntity<>(appendTraceInfo(result, ex), status);
    }

    //400
    @ExceptionHandler({
            HttpMediaTypeNotSupportedException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
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
        log.warn(String.format("[from: %s, status: 400, message: %s]", FROM, message));
        return appendTraceInfo(result, ex);
    }

    //401
    @ExceptionHandler({UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResult handleUnauthorized(HttpServletRequest req, Exception ex) {
        ErrorResult result = new ErrorResult(RestErrorResultCode.UNAUTHORIZED, "unauthorized request");
        log.info(String.format("[from: %s, status: 401, message: %s]", FROM, ex.getMessage()));
        return appendTraceInfo(result, ex);
    }

    //403
    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResult handleForbiddenException(HttpServletRequest req, Exception ex) {
        ErrorResult result = new ErrorResult(RestErrorResultCode.FORBIDDEN, "forbidden request");
        log.info(String.format("[from: %s, status: 403, message: %s]", FROM, ex.getMessage()));
        return appendTraceInfo(result, ex);
    }

    //404
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResult handleNoHandlerFound(HttpServletRequest req, Exception ex) {
        ErrorResult result = new ErrorResult(RestErrorResultCode.NOT_FOUND, "no handler found");
        log.info(String.format("[from: %s, status: 404, message: %s]", FROM, ex.getMessage()));
        return appendTraceInfo(result, ex);
    }

    //405
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResult handleMethodNotSupported(HttpServletRequest req, Exception ex) {
        ErrorResult result = new ErrorResult(RestErrorResultCode.METHOD_NOT_ALLOWED, "method not allowed");
        log.info(String.format("[from: %s, status: 405, message: %s]", FROM, ex.getMessage()));
        return appendTraceInfo(result, ex);
    }

    //500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult handleServerError(HttpServletRequest req, Exception ex) {
        ErrorResult result = new ErrorResult(RestErrorResultCode.INTERNAL_SERVER_ERROR, "server error");
        log.error(String.format("[from: %s, status: 500, message: %s]", FROM, ex.getMessage()), ex);
        return appendTraceInfo(result, ex);
    }

    private ErrorResult appendTraceInfo(ErrorResult result, Exception ex) {
        if (debug != null && debug) {
            result = new DebugErrorResult(result, new TraceInfo(ex));
        }
        return result;
    }
}
