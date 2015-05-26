package com.yunsoo.common.web.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions:
 */
public class RestClient {
    private static final int CONNECT_TIMEOUT = 3 * 1000; //3 seconds
    private static final int READ_TIMEOUT = 10 * 60 * 1000; //10 minutes

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
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);

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

    public <T> T get(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        ResponseEntity<T> result = restTemplate.exchange(createURL(url), HttpMethod.GET, null, responseType, uriVariables);
        return result.getBody();
    }

    @SuppressWarnings(value = "unchecked")
    public <T> Page<T> getPaged(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        ResponseEntity<T> result = restTemplate.exchange(createURL(url), HttpMethod.GET, null, responseType, uriVariables);
        T resultContent = result.getBody();
        int page = 0;
        int total = 1;
        List<String> pagesValue = result.getHeaders().get("Content-Range");
        if (pagesValue != null && pagesValue.size() == 1) {
            String[] pagesArray = pagesValue.get(0).replace("pages ", "").split("/");
            try {
                page = Integer.parseInt(pagesArray[0]);
                total = Integer.parseInt(pagesArray[1]);
            } catch (NumberFormatException ex) {
                page = 0;
                total = 1;
            }
        }

        return new Page(resultContent, page, total);
    }

    //POST
    public <T> T post(String url, Object request, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForObject(createURL(url), request, responseType, uriVariables);
    }

    public void post(String url, Object request, Object... uriVariables) {
        restTemplate.postForObject(createURL(url), request, String.class, uriVariables);
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
