package com.yunsoo.api.data;

import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;
import com.yunsoo.common.web.exception.RestErrorResultException;

/**
 * Created by:   Lijian
 * Created on:   2015/3/6
 * Descriptions:
 */
public class DataAPIClient extends RestClient {

    public DataAPIClient(String baseURL) {
        super(baseURL, new RestResponseErrorHandler());
    }

    @Override
    public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        try {
            return super.get(url, responseType, uriVariables);
        } catch (RestErrorResultException ex) {
            throw ex;
        }
    }

    @Override
    public <T> T post(String url, Object request, Class<T> responseType, Object... uriVariables) {
        try {
            return super.post(url, request, responseType, uriVariables);
        } catch (RestErrorResultException ex) {
            throw ex;
        }
    }

    @Override
    public void put(String url, Object request, Object... uriVariables) {
        try {
            super.put(url, request, uriVariables);
        } catch (RestErrorResultException ex) {
            throw ex;
        }
    }

    @Override
    public void patch(String url, Object request, Object... uriVariables) {
        try {
            super.patch(url, request, uriVariables);
        } catch (RestErrorResultException ex) {
            throw ex;
        }
    }

    @Override
    public void delete(String url, Object... uriVariables) {
        try {
            super.delete(url, uriVariables);
        } catch (RestErrorResultException ex) {
            throw ex;
        }
    }

}
