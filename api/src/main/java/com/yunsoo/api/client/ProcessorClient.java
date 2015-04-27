package com.yunsoo.api.client;

import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.client.RestResponseErrorHandler;

/**
 * Created by:   Lijian
 * Created on:   2015/4/3
 * Descriptions:
 */
public class ProcessorClient extends RestClient {

    public ProcessorClient(String baseURL) {
        super(baseURL, new RestResponseErrorHandler());
    }


}
