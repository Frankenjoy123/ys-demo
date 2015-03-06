package com.yunsoo.common.web.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.exception.APIErrorResultException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2015/3/6
 * Descriptions:
 */
public class RestResponseErrorHandler implements ResponseErrorHandler {

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    public void handleError(ClientHttpResponse response) throws APIErrorResultException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResult result;
        HttpStatus statusCode;
        try {
            statusCode = response.getStatusCode();
            result = mapper.readValue(response.getBody(), ErrorResult.class);
        } catch (Exception ex) {
            throw new InternalServerErrorException().withInnerException(ex);
        }
        throw new APIErrorResultException(statusCode, result);
    }
}
