
package com.yunsoo.api.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */

public class DataAPIClient {

    private URI baseURI;

    @Autowired
    private RestTemplate restTemplate;

    public DataAPIClient(URI baseURI) {
        this.baseURI = baseURI;
    }

    public URI getBaseURI() {
        return baseURI;
    }


    public <T> T getRequest(String path, Class<T> responseType, Map<String, String> uriVariables) {
        try {
            return restTemplate.getForObject(baseURI.resolve(path).toString(), responseType, uriVariables);
        } catch (RestClientException ex) {
            return null; //todo
        }
    }

    public <T> T getRequest(String path, Class<T> responseType) {
        try {
            return restTemplate.getForObject(baseURI.resolve(path).toString(), responseType);
        } catch (RestClientException ex) {
            return null; //todo
        }
    }
}
