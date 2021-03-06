package com.yunsoo.common.web.client;

import com.yunsoo.common.web.Constants;
import com.yunsoo.common.web.health.Health;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
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
 *
 * @see RestTemplate
 * @see HttpComponentsClientHttpRequestFactory
 */
public class RestClient {

    protected static final int CONNECT_TIMEOUT = 3 * 1000; //3 seconds
    protected static final int READ_TIMEOUT = 10 * 60 * 1000; //10 minutes

    private String baseUrl;

    protected CustomRestTemplate restTemplate;


    public RestClient() {
        this(null);
    }

    public RestClient(String baseUrl) {
        this(baseUrl, createRequestFactory());
    }

    protected RestClient(String baseUrl, ClientHttpRequestFactory requestFactory) {
        this.baseUrl = baseUrl;
        if (baseUrl != null && !baseUrl.endsWith("/")) {
            this.baseUrl += "/";
        }

        CustomRestTemplate restTemplate = new CustomRestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new RestResponseErrorHandler());
        this.restTemplate = restTemplate;
    }

    //create ClientHttpRequestFactory with PATCH method support
    private static ClientHttpRequestFactory createRequestFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);
        return requestFactory;
    }

    public void setErrorHandler(ResponseErrorHandler responseErrorHandler) {
        Assert.notNull(responseErrorHandler, "responseErrorHandler must not be null");
        this.restTemplate.setErrorHandler(responseErrorHandler);
    }

    public void setPreRequestCallback(PreRequestCallback preRequestCallback) {
        Assert.notNull(preRequestCallback, "preRequestCallback must not be null");
        this.restTemplate.setPreRequestCallback(preRequestCallback);
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    public Health checkHealth() {
        return checkHealth(null, false);
    }

    public Health checkHealth(List<String> path, boolean debug) {
        try {
            QueryStringBuilder builder = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK);
            if (path != null && path.size() > 0) builder.append("path", path);
            if (debug) builder.append("debug", true);
            return get("health" + builder.build(), Health.class);
        } catch (Exception e) {
            return new Health(Health.Status.DOWN).withDetail("exception", e.getMessage());
        }
    }


    //GET
    public <T> T get(String url, Class<T> responseType, Object... uriVariables) {
        Assert.notNull(responseType, "responseType must not be null");
        return restTemplate.getForObject(getAbsoluteUrl(url), responseType, uriVariables);
    }

    public <T> T get(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        Assert.notNull(responseType, "responseType must not be null");
        return restTemplate.exchange(getAbsoluteUrl(url), HttpMethod.GET, null, responseType, uriVariables).getBody();
    }

    public <T> Page<T> getPaged(String url, ParameterizedTypeReference<List<T>> responseType, Object... uriVariables) {
        return restTemplate.execute(getAbsoluteUrl(url), HttpMethod.GET, null, getPageResponseExtractor(responseType), uriVariables);
    }

    public ResourceInputStream getResourceInputStream(String url, Object... uriVariables) {
        RequestCallback requestCallback = request -> request.getHeaders().set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        return restTemplate.execute(getAbsoluteUrl(url), HttpMethod.GET, requestCallback, getResourceInputStreamResponseExtractor(), uriVariables);
    }

    //POST
    public <T> T post(String url, Object request, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForEntity(getAbsoluteUrl(url), request, responseType, uriVariables).getBody();
    }

    //PUT
    public void put(String url, Object request, Object... uriVariables) {
        restTemplate.put(getAbsoluteUrl(url), request, uriVariables);
    }

    public void put(String url, ResourceInputStream resourceInputStream, Object... uriVariables) {
        RequestCallback requestCallback = request -> {
            HttpHeaders httpHeaders = request.getHeaders();
            httpHeaders.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
            httpHeaders.setContentLength(resourceInputStream.getContentLength());
            httpHeaders.setContentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
            OutputStream outputStream = request.getBody();
            StreamUtils.copy(resourceInputStream, outputStream);
        };
        restTemplate.execute(getAbsoluteUrl(url), HttpMethod.PUT, requestCallback, null, uriVariables);
    }


    //PATCH
    public void patch(String url, Object request, Object... uriVariables) {
        restTemplate.exchange(getAbsoluteUrl(url), HttpMethod.PATCH, new HttpEntity<>(request), (Class<?>) null, uriVariables);
    }

    //DELETE
    public void delete(String url, Object... uriVariables) {
        restTemplate.delete(getAbsoluteUrl(url), uriVariables);
    }


    protected String getAbsoluteUrl(String url) {
        Assert.notNull(url, "url must not be null");
        if (baseUrl != null && url.startsWith("/")) {
            url = url.substring(1);
        }
        return baseUrl + url;
    }

    protected <T> ResponseExtractor<Page<T>> getPageResponseExtractor(ParameterizedTypeReference<List<T>> responseType) {
        Assert.notNull(responseType, "responseType must not be null");
        return response -> {
            ResponseExtractor<List<T>> resExt = new HttpMessageConverterExtractor<>(responseType.getType(), restTemplate.getMessageConverters());
            List<T> content = resExt.extractData(response);
            Integer page = 0, total = null, count = null;
            List<String> pagesValue = response.getHeaders().get(Constants.HttpHeaderName.CONTENT_RANGE);
            if (pagesValue == null || pagesValue.size() == 0) {
                pagesValue = response.getHeaders().get("Content-Range");
            }
            if (pagesValue != null && pagesValue.size() > 0) {
                Integer[] pagesArray = PageableUtils.parsePages(pagesValue.get(0));
                page = pagesArray[0];
                total = pagesArray[1];
                count = pagesArray[2];
            }
            return new Page<>(content, page, total, count);
        };
    }

    protected ResponseExtractor<ResourceInputStream> getResourceInputStreamResponseExtractor() {
        return response -> {
            HttpHeaders httpHeaders = response.getHeaders();
            InputStream inputStream = response.getBody();
            long contentLength = httpHeaders.getContentLength();
            String contentType = httpHeaders.getContentType().toString();
            return new ResourceInputStream(new ByteArrayInputStream(StreamUtils.copyToByteArray(inputStream)), contentLength, contentType);
        };
    }


    private static class CustomRestTemplate extends RestTemplate {

        private PreRequestCallback preRequestCallback;

        public CustomRestTemplate() {
            super();
        }

        public void setPreRequestCallback(PreRequestCallback preRequestCallback) {
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
