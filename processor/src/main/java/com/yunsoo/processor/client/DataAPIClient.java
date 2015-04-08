package com.yunsoo.processor.client;

import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;

/**
 * Created by:   Lijian
 * Created on:   2015/4/8
 * Descriptions:
 */
public class DataAPIClient extends RestClient {

    public DataAPIClient(String baseURL) {
        super(baseURL, new RestResponseErrorHandler());
    }
}
