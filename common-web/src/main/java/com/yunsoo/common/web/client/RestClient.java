
package com.yunsoo.common.web.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
public class RestClient {

    private RestTemplate restTemplate;
    private String baseURL;

    public RestClient() {
        this(null, null);
    }

    public RestClient(String baseURL) {
        this(baseURL, null);
    }

    public RestClient(String baseURL, ResponseErrorHandler responseErrorHandler) {
        //added this request factory for PATCH method support
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        this.restTemplate = new RestTemplate(requestFactory);

        this.baseURL = baseURL;
        if (baseURL != null && !baseURL.endsWith("/")) {
            this.baseURL += "/";
        }

        if (responseErrorHandler != null) {
            restTemplate.setErrorHandler(responseErrorHandler);
        }
    }

    public String getBaseURL() {
        return baseURL;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }


    private String createURL(String url) {
        Assert.notNull(url, "'url' must not be null");
        if (baseURL != null && url.startsWith("/")) {
            url = url.substring(1);
        }
        return baseURL + url;
    }


    //GET
    public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForObject(createURL(url), responseType, uriVariables);
    }


    //POST
    public <T> T post(String url, Object request, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForObject(createURL(url), request, responseType, uriVariables);
    }

    //PUT
    public void put(String url, Object request, Object... uriVariables) {
        restTemplate.put(createURL(url), request, uriVariables);
    }

    //PATCH
    public void patch(String url, Object request, Object... uriVariables) {
        restTemplate.exchange(createURL(url), HttpMethod.PATCH, new HttpEntity<>(request), (Class<?>) null, uriVariables);
    }

    //DELETE
    public void delete(String url, Object... uriVariables) {
        restTemplate.delete(createURL(url), uriVariables);
    }
}
