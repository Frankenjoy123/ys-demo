package com.yunsoo.common.web.client;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.util.HexStringUtils;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.MockRestApiServer.MockObject;
import com.yunsoo.common.web.exception.*;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by:   Lijian
 * Created on:   2016-07-22
 * Descriptions:
 */
public class RestClientTest extends ClientTestBase {

    private static RestClient restClient;

    protected RestClient getRestClient() {
        return restClient;
    }

    @Before
    public void initRestClient() {
        if (restClient == null) {
            System.out.println("initializing restClient");
            restClient = new RestClient("http://localhost:" + port);
            restClient.setPreRequestCallback(request -> {
                request.getHeaders().set(HttpHeaders.USER_AGENT, "UnitTestMethod");
            });
        }
    }

    @Test
    public void test_getObject() {
        String name = "Hello";
        MockObject mockObject = getRestClient().get("get/object/{name}", MockObject.class, name);
        assert name.equals(mockObject.getName());
        System.out.println("return from get: " + StringFormatter.formatMap("name", mockObject.getName(), "value", mockObject.getValue()));

        mockObject = getRestClient().get("get/object/{name}", new ParameterizedTypeReference<MockObject>() {
        }, name);
        assert name.equals(mockObject.getName());
        System.out.println("return from get: " + StringFormatter.formatMap("name", mockObject.getName(), "value", mockObject.getValue()));
    }

    @Test
    public void test_getPaged() {
        String name = "Hello";
        Page<MockObject> page = getRestClient().getPaged(
                "get/page" + new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("name", name).append(new PageRequest(1, 5)).build(),
                new ParameterizedTypeReference<List<MockObject>>() {
                }, name);
        System.out.println("return from page: " +
                StringFormatter.formatMap(
                        "page", page.getPage(),
                        "total", page.getTotal(),
                        "count", page.getCount(),
                        "contentSize", page.getContent().size()));
    }

    @Test
    public void test_getResourceInputStream() throws IOException {
        ResourceInputStream resourceInputStream = getRestClient().getResourceInputStream("get/stream");
        String contentType = resourceInputStream.getContentType();
        long contentLength = resourceInputStream.getContentLength();
        byte[] content = StreamUtils.copyToByteArray(resourceInputStream);
        System.out.println("return from stream: " +
                StringFormatter.formatMap(
                        "contentType", contentType,
                        "contentLength", contentLength,
                        "content", HexStringUtils.encode(content)));
    }

    @Test
    public void test_post() {
        String name = "Hello";
        MockObject mockObject = getRestClient().post("post", new MockObject(name, "test"), MockObject.class);
        assert name.equals(mockObject.getName());
    }

    @Test
    public void test_patch() {
        String name = "Hello";
        getRestClient().patch("patch", new MockObject(name, "test"), MockObject.class);
    }

    @Test
    public void test_put() {
        String name = "Hello";
        getRestClient().put("put", new MockObject(name, "test"), MockObject.class);
    }

    @Test
    public void test_putResourceInputStream() {
        getRestClient().put("put/stream", new ResourceInputStream(new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43}), 3, "text/plain"));
    }

    @Test
    public void test_delete() throws ExecutionException, InterruptedException {
        getRestClient().delete("delete");
    }

    @Test()
    public void test_error() {
        try {
            getRestClient().get("error/400", ErrorResult.class);
        } catch (BadRequestException e) {
            System.out.println(e.getErrorResult().toString());
        }

        try {
            getRestClient().get("error/401", ErrorResult.class);
        } catch (UnauthorizedException e) {
            System.out.println(e.getErrorResult().toString());
        }

        try {
            getRestClient().get("error/403", ErrorResult.class);
        } catch (ForbiddenException e) {
            System.out.println(e.getErrorResult().toString());
        }

        try {
            getRestClient().get("error/404", ErrorResult.class);
        } catch (NotFoundException e) {
            System.out.println(e.getErrorResult().toString());
        }

        try {
            getRestClient().get("error/500", ErrorResult.class);
        } catch (InternalServerErrorException e) {
            System.out.println(e.getErrorResult().toString());
        }
    }

}
