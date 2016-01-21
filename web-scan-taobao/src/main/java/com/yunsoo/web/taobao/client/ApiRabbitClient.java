package com.yunsoo.web.taobao.client;

import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;
import com.yunsoo.web.taobao.Constants;
import org.springframework.http.HttpHeaders;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
public class ApiRabbitClient extends RestClient {

    public ApiRabbitClient(String baseURL) {
        super(baseURL, new RestResponseErrorHandler(), request -> {
            HttpHeaders httpHeaders = request.getHeaders();
            httpHeaders.set(Constants.HttpHeaderName.APP_ID, Constants.APP_ID);
            httpHeaders.set(Constants.HttpHeaderName.DEVICE_ID, Constants.DEVICE_ID);
        });
    }

}
