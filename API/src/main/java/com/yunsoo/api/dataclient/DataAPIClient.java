package com.yunsoo.api.dataclient;

import java.net.URI;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
public class DataAPIClient {

    private URI baseURI;

    public DataAPIClient(URI baseURI){
        this.baseURI = baseURI;
    }

    public URI getBaseURI() {
        return baseURI;
    }
}
