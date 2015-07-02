package com.yunsoo.api.client;

import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;

/**
 * Created by:   Lijian
 * Created on:   2015/3/6
 * Descriptions:
 */
public class DataAPIClient extends RestClient {

    public DataAPIClient(String baseURL) {
        super(baseURL, new RestResponseErrorHandler());
    }

//    //GET
//
//    @Override
//    public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
//        return super.get(url, responseType, uriVariables);
//    }
//
//    @Override
//    public <T> T get(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
//        return super.get(url, responseType, uriVariables);
//    }
//
//    @Override
//    public <T> Page<T> getPaged(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
//        return super.getPaged(url, responseType, uriVariables);
//    }

}
