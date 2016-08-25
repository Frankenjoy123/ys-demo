package com.yunsoo.common.web.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.*;

import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-14
 * Descriptions: Asynchronous client-side HTTP access. Wrapper of {@link AsyncRestTemplate}
 *
 * @see AsyncRestTemplate
 * @see HttpComponentsAsyncClientHttpRequestFactory
 */
public class AsyncRestClient extends RestClient {

    protected CustomAsyncRestTemplate asyncRestTemplate;

    public AsyncRestClient() {
        this(null);
    }

    public AsyncRestClient(String baseUrl) {
        this(baseUrl, createRequestFactory());
    }

    private AsyncRestClient(String baseUrl, HttpComponentsAsyncClientHttpRequestFactory requestFactory) {
        super(baseUrl, requestFactory);
        this.asyncRestTemplate = new CustomAsyncRestTemplate(requestFactory, restTemplate);
    }

    private static HttpComponentsAsyncClientHttpRequestFactory createRequestFactory() {
        HttpComponentsAsyncClientHttpRequestFactory requestFactory = new HttpComponentsAsyncClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);
        return requestFactory;
    }

    @Override
    public void setPreRequestCallback(PreRequestCallback preRequestCallback) {
        Assert.notNull(preRequestCallback, "preRequestCallback must not be null");
        super.setPreRequestCallback(preRequestCallback);
        this.asyncRestTemplate.setPreRequestCallback(preRequestCallback);
    }

    public AsyncRestTemplate getAsyncRestTemplate() {
        return this.asyncRestTemplate;
    }


    //GET
    public <T> ListenableFuture<T> getAsync(String url, Class<T> responseType, Object... uriVariables) {
        Assert.notNull(responseType, "responseType must not be null");
        return asyncRestTemplate.execute(getAbsoluteUrl(url), HttpMethod.GET, null, responseType, uriVariables);
    }

    public <T> ListenableFuture<T> getAsync(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        Assert.notNull(responseType, "responseType must not be null");
        return asyncRestTemplate.execute(getAbsoluteUrl(url), HttpMethod.GET, null, responseType.getType(), uriVariables);
    }

    public <T> ListenableFuture<Page<T>> getPagedAsync(String url, ParameterizedTypeReference<List<T>> responseType, Object... uriVariables) {
        return asyncRestTemplate.execute(getAbsoluteUrl(url), HttpMethod.GET, null, getPageResponseExtractor(responseType), uriVariables);
    }

    public ListenableFuture<ResourceInputStream> getResourceInputStreamAsync(String url, Object... uriVariables) {
        AsyncRequestCallback requestCallback = request -> request.getHeaders().set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        return asyncRestTemplate.execute(getAbsoluteUrl(url), HttpMethod.GET, requestCallback, getResourceInputStreamResponseExtractor(), uriVariables);
    }


    //POST
    public <T> ListenableFuture<T> postAsync(String url, Object request, Class<T> responseType, Object... uriVariables) {
        return asyncRestTemplate.execute(getAbsoluteUrl(url), HttpMethod.POST, request, responseType, uriVariables);
    }

    //put
    public ListenableFuture<?> putAsync(String url, Object request, Object... uriVariables) {
        return asyncRestTemplate.execute(getAbsoluteUrl(url), HttpMethod.PUT, request, null, uriVariables);
    }

    public ListenableFuture<?> putAsync(String url, ResourceInputStream resourceInputStream, Object... uriVariables) {
        AsyncRequestCallback requestCallback = request -> {
            HttpHeaders httpHeaders = request.getHeaders();
            httpHeaders.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
            httpHeaders.setContentLength(resourceInputStream.getContentLength());
            httpHeaders.setContentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
            OutputStream outputStream = request.getBody();
            StreamUtils.copy(resourceInputStream, outputStream);
        };
        return asyncRestTemplate.execute(getAbsoluteUrl(url), HttpMethod.PUT, requestCallback, (ResponseExtractor<?>) null, uriVariables);
    }


    //PATCH
    public ListenableFuture<?> patchAsync(String url, Object request, Object... uriVariables) {
        return asyncRestTemplate.execute(getAbsoluteUrl(url), HttpMethod.PATCH, request, null, uriVariables);
    }


    //DELETE
    public ListenableFuture<?> deleteAsync(String url, Object... uriVariables) {
        return asyncRestTemplate.execute(getAbsoluteUrl(url), HttpMethod.DELETE, (Object) null, null, uriVariables);
    }


    private static class CustomAsyncRestTemplate extends AsyncRestTemplate {

        private PreRequestCallback preRequestCallback;

        public CustomAsyncRestTemplate(AsyncClientHttpRequestFactory requestFactory, RestTemplate restTemplate) {
            super(requestFactory, restTemplate);
        }

        public void setPreRequestCallback(PreRequestCallback preRequestCallback) {
            this.preRequestCallback = preRequestCallback;
        }

        public <T> ListenableFuture<T> execute(String url, HttpMethod method, Object request, Type responseType, Object... urlVariables) {
            URI expanded = getUriTemplateHandler().expand(url, urlVariables);
            HttpEntity<?> requestEntity = request instanceof HttpEntity ? (HttpEntity<?>) request : request != null ? new HttpEntity<>(request) : null;
            AsyncRequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
            HttpMessageConverterExtractor<T> responseExtractor = responseType != null ? new HttpMessageConverterExtractor<>(responseType, getMessageConverters()) : null;
            return doExecute(expanded, method, requestCallback, responseExtractor);
        }

        @Override
        protected <T> ListenableFuture<T> doExecute(URI url, HttpMethod method, AsyncRequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
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
