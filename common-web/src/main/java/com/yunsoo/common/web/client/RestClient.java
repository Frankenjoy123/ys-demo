package com.yunsoo.common.web.client;

import com.yunsoo.common.web.util.PageableUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/2
 * Descriptions: Synchronous client-side HTTP access. Wrapper of {@link RestTemplate}
 */
public class RestClient {
    private static final int CONNECT_TIMEOUT = 3 * 1000; //3 seconds
    private static final int READ_TIMEOUT = 10 * 60 * 1000; //10 minutes

    private String baseUrl;

    private CustomRestTemplate restTemplate;


    public RestClient() {
        this(null);
    }

    public RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        if (baseUrl != null && !baseUrl.endsWith("/")) {
            this.baseUrl += "/";
        }

        //added this request factory for PATCH method support
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);

        this.restTemplate = new CustomRestTemplate();
        this.restTemplate.setRequestFactory(requestFactory);
        this.restTemplate.setErrorHandler(new RestResponseErrorHandler());
    }

    public void setErrorHandler(ResponseErrorHandler responseErrorHandler) {
        Assert.notNull(responseErrorHandler, "responseErrorHandler must not be null");
        this.restTemplate.setErrorHandler(responseErrorHandler);
    }

    public void setPreRequestCallback(RequestCallback preRequestCallback) {
        Assert.notNull(preRequestCallback, "preRequestCallback must not be null");
        this.restTemplate.setPreRequestCallback(preRequestCallback);
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }


    //GET
    public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForObject(createURL(url), responseType, uriVariables);
    }

    public <T> T get(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        ResponseEntity<T> result = restTemplate.exchange(createURL(url), HttpMethod.GET, null, responseType, uriVariables);
        return result.getBody();
    }

    public <T> Page<T> getPaged(String url, ParameterizedTypeReference<List<T>> responseType, Object... uriVariables) {
        ResponseEntity<List<T>> result = restTemplate.exchange(createURL(url), HttpMethod.GET, null, responseType, uriVariables);
        List<T> resultContent = result.getBody();
        Integer page = 0, total = null, count = null;
        List<String> pagesValue = result.getHeaders().get("Content-Range");
        if (pagesValue != null && pagesValue.size() == 1) {
            Integer[] pagesArray = PageableUtils.parsePages(pagesValue.get(0));
            page = pagesArray[0];
            total = pagesArray[1];
            count = pagesArray[2];
        }
        return new Page<>(resultContent, page, total, count);
    }

    public ResourceInputStream getResourceInputStream(String url, Object... uriVariables) {
        RequestCallback requestCallback = request ->
                request.getHeaders().set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        ResponseExtractor<ResourceInputStream> responseExtractor = response -> {
            HttpHeaders httpHeaders = response.getHeaders();
            InputStream inputStream = response.getBody();
            long contentLength = httpHeaders.getContentLength();
            String contentType = httpHeaders.getContentType().toString();
            return new ResourceInputStream(new ByteArrayInputStream(StreamUtils.copyToByteArray(inputStream)), contentLength, contentType);
        };
        return restTemplate.execute(createURL(url), HttpMethod.GET, requestCallback, responseExtractor, uriVariables);
    }

    //POST
    public <T> T post(String url, Object request, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForObject(createURL(url), request, responseType, uriVariables);
    }

    //PUT
    public void put(String url, Object request, Object... uriVariables) {
        restTemplate.put(createURL(url), request, uriVariables);
    }

    public void put(String url, ResourceInputStream resourceInputStream, Object... uriVariables) {
        RequestCallback requestCallback = request -> {
            HttpHeaders httpHeaders = request.getHeaders();
            //httpHeaders.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
            httpHeaders.setContentLength(resourceInputStream.getContentLength());
            httpHeaders.setContentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
            OutputStream outputStream = request.getBody();
            StreamUtils.copy(resourceInputStream, outputStream);
        };
        restTemplate.execute(createURL(url), HttpMethod.PUT, requestCallback, null, uriVariables);
    }


    //PATCH
    public void patch(String url, Object request, Object... uriVariables) {
        restTemplate.exchange(createURL(url), HttpMethod.PATCH, new HttpEntity<>(request), (Class<?>) null, uriVariables);
    }

    //DELETE
    public void delete(String url, Object... uriVariables) {
        restTemplate.delete(createURL(url), uriVariables);
    }


    private String createURL(String url) {
        Assert.notNull(url, "url must not be null");
        if (baseUrl != null && url.startsWith("/")) {
            url = url.substring(1);
        }
        return baseUrl + url;
    }


    private static class CustomRestTemplate extends RestTemplate {

        private RequestCallback preRequestCallback;

        public CustomRestTemplate() {
            super();
        }

        public void setPreRequestCallback(RequestCallback preRequestCallback) {
            this.preRequestCallback = preRequestCallback;
        }

        @Override
        protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
            return super.doExecute(url, method, request -> {
                if (this.preRequestCallback != null) {
                    this.preRequestCallback.doWithRequest(request);
                }
                if (requestCallback != null) {
                    requestCallback.doWithRequest(request);
                }
            }, responseExtractor);
        }
    }
}
