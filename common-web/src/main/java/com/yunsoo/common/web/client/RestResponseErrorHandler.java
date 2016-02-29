package com.yunsoo.common.web.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.web.exception.*;
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

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws RestErrorResultException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResult result;
        HttpStatus statusCode;
        try {
            statusCode = response.getStatusCode();
            result = mapper.readValue(response.getBody(), ErrorResult.class);
        } catch (Exception ex) {
            throw new InternalServerErrorException().withInnerException(ex);
        }
        switch (statusCode) {
            case BAD_REQUEST: //400
                throw new BadRequestException(result);

            case UNAUTHORIZED: //401
                throw new UnauthorizedException(result);

            case FORBIDDEN: //403
                throw new ForbiddenException(result);

            case NOT_FOUND: //404
                throw new NotFoundException(result);

            case NOT_ACCEPTABLE: //406
                throw new NotAcceptableException(result);

            case CONFLICT: //409
                throw new ConflictException(result);

            case UNPROCESSABLE_ENTITY: //422
                throw new UnprocessableEntityException(result);

            case INTERNAL_SERVER_ERROR: //500
                throw new InternalServerErrorException(result);

            default:
                throw new RestErrorResultException(statusCode, result);
        }
    }
}