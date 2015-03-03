
package com.yunsoo.api.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
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

    private String baseURL;

    @Autowired
    private RestTemplate restTemplate;

    public DataAPIClient(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }


    //GET

    public <T> T get(String path, Class<T> responseType, Object... uriVariables) {
        try {
            return restTemplate.getForObject(baseURL + path, responseType, uriVariables);
        } catch (HttpClientErrorException ex) {
            //todo:handle error code/message from dataAPI
            System.out.println(ex.getResponseBodyAsString());
            return null;
        } catch (RestClientException ex) {
            throw ex;
        }
    }

//    public <T> T get(String path, Class<T> responseType, Map<String, String> uriVariables) {
//        try {
//            return restTemplate.getForObject(baseURL + path, responseType, uriVariables);
//        } catch (RestClientException ex) {
//            throw ex;
//            //return null; //todo
//        }
//    }


    //POST


    //PUT


    //PATCH


    //DELETE
}
