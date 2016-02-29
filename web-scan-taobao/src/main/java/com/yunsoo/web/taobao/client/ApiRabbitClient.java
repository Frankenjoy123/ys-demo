package com.yunsoo.web.taobao.client;

import com.yunsoo.web.taobao.Constants;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
/*
public class ApiRabbitClient extends RestClient {

    public ApiRabbitClient(String baseURL) {

        super(baseURL, new RestResponseErrorHandler(), new RequestCallback() {
            @Override
            public void doWithRequest(ClientHttpRequest request) throws IOException {
                request.getHeaders().set(Constants.HttpHeaderName.APP_ID, Constants.APP_ID);
                request.getHeaders().set(Constants.HttpHeaderName.DEVICE_ID, Constants.DEVICE_ID);
            }
        });

    }

}
*/
public class ApiRabbitClient {

    public ApiRabbitClient(String baseURL) {


    }

}
