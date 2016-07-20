package com.yunsoo.common.web.client;

import org.springframework.web.client.AsyncRestTemplate;

/**
 * Created by:   Lijian
 * Created on:   2016-07-14
 * Descriptions: Asynchronous client-side HTTP access. Wrapper of {@link AsyncRestTemplate}
 */
public class AsyncRestClient {

    private String baseUrl;

    private AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

    public AsyncRestClient() {
        this(null);
    }

    public AsyncRestClient(String baseUrl) {
        this.baseUrl = baseUrl;


    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

}
