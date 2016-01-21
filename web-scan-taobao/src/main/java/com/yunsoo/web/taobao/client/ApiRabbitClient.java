package com.yunsoo.web.taobao.client;

import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
public class ApiRabbitClient extends RestClient {

    public ApiRabbitClient(String baseURL) {
        super(baseURL, new RestResponseErrorHandler());
    }

}
