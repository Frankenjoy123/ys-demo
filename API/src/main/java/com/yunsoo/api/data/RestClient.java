
package com.yunsoo.api.data;

import com.yunsoo.common.web.exception.APIErrorResultException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */

public class RestClient {

    private String baseURL;

    private RestTemplate restTemplate;

    public RestClient(String baseURL) {
        this.baseURL = baseURL;
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DataAPIErrorHandler());
    }

    public String getBaseURL() {
        return baseURL;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }


    private String createURL(String path) {
        Assert.notNull(path, "'path' must not be null");
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return baseURL + path;
    }


    //GET

    public <T> T get(String path, Class<T> responseType, Object... uriVariables) {
        try {
            return restTemplate.getForObject(createURL(path), responseType, uriVariables);
        } catch (APIErrorResultException ex) {
            //todo:handle error code/message from dataAPI
            //System.out.println(ex.getResponseBodyAsString());
            return null;
        } catch (Exception ex) {
            throw ex;
        }
    }

//    public <T> T get(String path, Class<T> responseType, Map<String, String> uriVariables) {
//        try {
//            return restTemplate.getForObject(createURL(path), responseType, uriVariables);
//        } catch (RestClientException ex) {
//            throw ex;
//            //return null; //todo
//        }
//    }


    //POST

    public <T> T post(String path, Object request, Class<T> responseType, Object... uriVariables) {
        try {
            return restTemplate.postForObject(createURL(path), request, responseType, uriVariables);
        } catch (HttpClientErrorException ex) {
            //todo:handle error code/message from dataAPI
            return null;
        } catch (RestClientException ex) {
            throw ex;
        }
    }

    //PUT

    public void put(String path, Object request, Object... uriVariables) {
        try {
            restTemplate.put(createURL(path), request, uriVariables);
        } catch (HttpClientErrorException ex) {
            //todo:handle error code/message from dataAPI

        } catch (RestClientException ex) {
            throw ex;
        }
    }

    //PATCH

    public void patch(String path, Object request, Object... uriVariables) {
        try {
            HttpEntity<?> httpEntity = new HttpEntity<>(request);
            restTemplate.exchange(createURL(path), HttpMethod.PATCH, httpEntity, (Class<?>) null, uriVariables);
        } catch (HttpClientErrorException ex) {
            //todo:handle error code/message from dataAPI

        } catch (RestClientException ex) {
            throw ex;
        }
    }

    //DELETE

    public void delete(String path, Object... uriVariables) {
        try {
            restTemplate.delete(createURL(path), uriVariables);
        } catch (HttpClientErrorException ex) {
            //todo:handle error code/message from dataAPI

        } catch (RestClientException ex) {
            throw ex;
        }
    }
}
