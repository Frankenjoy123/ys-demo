package com.yunsoo.auth.api.controller.util;

import com.yunsoo.common.error.DebugErrorResult;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.error.TraceInfo;
import com.yunsoo.common.web.error.RestErrorResultCode;
import com.yunsoo.common.web.exception.RestErrorResultException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by:   Lijian
 * Created on:   2016-07-27
 * Descriptions:
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class RestErrorController implements ErrorController {

    private static final String FROM = "api-auth";

    private Log log = LogFactory.getLog(this.getClass());

    @Value("${yunsoo.debug}")
    private Boolean debug;

    private ErrorAttributes errorAttributes;

    private ErrorProperties errorProperties;

    @Autowired
    public RestErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        this.errorAttributes = errorAttributes;
        this.errorProperties = serverProperties.getError();
    }

    @RequestMapping
    public ResponseEntity<ErrorResult> handleRestError(HttpServletRequest request) {
        HttpStatus status;
        ErrorResult result;
        Throwable t = this.errorAttributes.getError(new ServletRequestAttributes(request));
        if (t instanceof RestErrorResultException) {
            RestErrorResultException ex = (RestErrorResultException) t;
            status = ex.getHttpStatus();
            result = ex.getErrorResult();
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result = new ErrorResult(RestErrorResultCode.INTERNAL_SERVER_ERROR, "server error");
        }
        log.info(String.format("[from: %s, status: %s, message: %s]", FROM, status, result));
        return new ResponseEntity<>(appendTraceInfo(result, t), status);
    }

    private ErrorResult appendTraceInfo(ErrorResult result, Throwable ex) {
        if (debug != null && debug) {
            result = new DebugErrorResult(result, new TraceInfo(ex));
        }
        return result;
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }
}
