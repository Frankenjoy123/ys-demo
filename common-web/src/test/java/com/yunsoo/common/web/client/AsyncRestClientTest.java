package com.yunsoo.common.web.client;

import com.yunsoo.common.error.ErrorResult;
import com.yunsoo.common.util.HexStringUtils;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.MockRestApiServer.MockObject;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by:   Lijian
 * Created on:   2016-07-22
 * Descriptions:
 */
public class AsyncRestClientTest extends RestClientTest {

    private static AsyncRestClient asyncRestClient;

    @Override
    protected RestClient getRestClient() {
        return asyncRestClient;
    }

    protected AsyncRestClient getAsyncRestClient() {
        return asyncRestClient;
    }


    @Before
    @Override
    public void initRestClient() {
        if (asyncRestClient == null) {
            System.out.println("initializing asyncRestClient");
            asyncRestClient = new AsyncRestClient("http://localhost:" + port);
            asyncRestClient.setPreRequestCallback(request -> {
                request.getHeaders().set(HttpHeaders.USER_AGENT, "UnitTestMethod");
            });
        }
    }


    @Test
    public void test_getObjectAsync() {
        String name = "Hello";
        Arrays.asList(getAsyncRestClient().getAsync("get/object/{name}", MockObject.class, name),
                getAsyncRestClient().getAsync("get/object/{name}", new ParameterizedTypeReference<MockObject>() {
                }, name)).stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .forEach(m -> {
                    assert m != null && name.equals(m.getName());
                    System.out.println("return from get: " + StringFormatter.formatMap("name", m.getName(), "value", m.getValue()));
                });
    }

    @Test
    public void test_getPagedAsync() throws ExecutionException, InterruptedException {
        String name = "Hello";
        Page<MockObject> page = getAsyncRestClient().getPagedAsync(
                "get/page" + new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("name", name).append(new PageRequest(1, 5)).build(),
                new ParameterizedTypeReference<List<MockObject>>() {
                }, name).get();
        System.out.println("return from page: " +
                StringFormatter.formatMap(
                        "page", page.getPage(),
                        "total", page.getTotal(),
                        "count", page.getCount(),
                        "contentSize", page.getContent().size()));
    }

    @Test
    public void test_getResourceInputStreamAsync() throws IOException, ExecutionException, InterruptedException {
        ResourceInputStream resourceInputStream = getAsyncRestClient().getResourceInputStreamAsync("get/stream").get();
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
    public void test_postAsync() throws ExecutionException, InterruptedException {
        String name = "Hello";
        MockObject mockObject = getAsyncRestClient()
                .postAsync("post", new MockObject(name, "test"), MockObject.class).get();
        assert name.equals(mockObject.getName());
    }

    @Test
    public void test_patchAsync() throws ExecutionException, InterruptedException {
        String name = "Hello";
        getAsyncRestClient().patchAsync("patch", new MockObject(name, "test"), MockObject.class).get();
    }

    @Test
    public void test_putAsync() throws ExecutionException, InterruptedException {
        String name = "Hello";
        getAsyncRestClient().putAsync("put", new MockObject(name, "test"), MockObject.class).get();
    }

    @Test
    public void test_putResourceInputStreamAsync() throws ExecutionException, InterruptedException {
        getAsyncRestClient().putAsync("put/stream", new ResourceInputStream(new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43}), 3, "text/plain")).get();
    }

    @Test
    public void test_deleteAsync() throws ExecutionException, InterruptedException {
        getAsyncRestClient().deleteAsync("delete").get();
    }

    @Test()
    public void test_errorAsync() {
//        getAsyncRestClient().getAsync("error/400", ErrorResult.class).addCallback(errorResult -> {
//        }, t -> {
//            assert t instanceof BadRequestException;
//            System.out.println(((BadRequestException) t).getErrorResult().toString());
//        });
//
//        getAsyncRestClient().getAsync("error/401", ErrorResult.class).addCallback(errorResult -> {
//        }, t -> {
//            assert t instanceof UnauthorizedException;
//            System.out.println(((UnauthorizedException) t).getErrorResult().toString());
//        });
//
//        getAsyncRestClient().getAsync("error/403", ErrorResult.class).addCallback(errorResult -> {
//        }, t -> {
//            assert t instanceof ForbiddenException;
//            System.out.println(((ForbiddenException) t).getErrorResult().toString());
//        });
//
//        getAsyncRestClient().getAsync("error/404", ErrorResult.class).addCallback(errorResult -> {
//        }, t -> {
//            assert t instanceof NotFoundException;
//            System.out.println(((NotFoundException) t).getErrorResult().toString());
//        });
//
//        getAsyncRestClient().getAsync("error/500", ErrorResult.class).addCallback(errorResult -> {
//        }, t -> {
//            assert t instanceof InternalServerErrorException;
//            System.out.println(((InternalServerErrorException) t).getErrorResult().toString());
//        });

        Arrays.asList(
                getAsyncRestClient().getAsync("error/400", ErrorResult.class),
                getAsyncRestClient().getAsync("error/401", ErrorResult.class),
                getAsyncRestClient().getAsync("error/403", ErrorResult.class),
                getAsyncRestClient().getAsync("error/404", ErrorResult.class),
                getAsyncRestClient().getAsync("error/500", ErrorResult.class)
        ).stream().forEach(f -> {
            try {
                f.get();
            } catch (ExecutionException e) {
                System.out.println("finally caught exception. " + e.getCause().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

}
