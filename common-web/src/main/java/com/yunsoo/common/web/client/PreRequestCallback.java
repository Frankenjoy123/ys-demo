package com.yunsoo.common.web.client;

import org.springframework.http.HttpRequest;

import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2016-07-20
 * Descriptions:
 */
public interface PreRequestCallback {

    void doWithRequest(HttpRequest request) throws IOException;

}
