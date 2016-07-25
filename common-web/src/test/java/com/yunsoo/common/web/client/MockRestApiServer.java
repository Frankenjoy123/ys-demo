package com.yunsoo.common.web.client;

import com.yunsoo.common.error.ErrorResult;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by:   Lijian
 * Created on:   2016-07-22
 * Descriptions:
 */
@ComponentScan(basePackages = "com.yunsoo.common.web.client")
@SpringBootApplication
public class MockRestApiServer {

    @RestController()
    @RequestMapping("/")
    public static class MockController {

        @RequestMapping(value = "get/object/{name}", method = RequestMethod.GET)
        public MockObject get(
                @RequestHeader(value = "User-Agent", required = true) String userAgent,
                @PathVariable String name) {
            return new MockObject(name, userAgent);
        }

        @RequestMapping(value = "get/page", method = RequestMethod.GET)
        public List<MockObject> getPaged(@RequestParam("name") String name, Pageable pageable, HttpServletResponse response) {
            int page = 0, size = 20;
            if (pageable != null) {
                page = pageable.getPageNumber();
                size = pageable.getPageSize();
            }
            int total = page + 2, count = size * total;
            if (pageable != null) {
                response.setHeader("Content-Range", new Page<>(new ArrayList<>(), page, total, count).toContentRange());
            }

            return IntStream.range(page * size, (page + 1) * size).mapToObj(i -> new MockObject(name, "test")).collect(Collectors.toList());
        }

        @RequestMapping(value = "get/stream", method = RequestMethod.GET)
        public ResponseEntity<?> getStream() {
            return new ResourceInputStream(new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43}), 3, "text/plain").toResponseEntity();
        }

        @ResponseStatus(HttpStatus.CREATED)
        @RequestMapping(value = "post", method = RequestMethod.POST)
        public MockObject post(@RequestBody MockObject mockObject) {
            return mockObject;
        }

        @RequestMapping(value = "patch", method = RequestMethod.PATCH)
        public void patch(@RequestBody MockObject mockObject) {
        }

        @RequestMapping(value = "put", method = RequestMethod.PUT)
        public void put(@RequestBody MockObject mockObject) {
        }

        @RequestMapping(value = "put/stream", method = RequestMethod.PUT)
        public void putStream(HttpServletRequest request) throws IOException {
            assert StringUtils.hasText(request.getContentType());
            assert request.getContentLengthLong() > 0;
            assert request.getInputStream().read() > 0;
        }

        @ResponseStatus(HttpStatus.NO_CONTENT)
        @RequestMapping(value = "delete", method = RequestMethod.DELETE)
        public void delete() {
        }

        @RequestMapping(value = "error/{code}")
        public ResponseEntity<ErrorResult> returnError(@PathVariable("code") int code) {
            return ResponseEntity.status(code).body(new ErrorResult(code, "test"));
        }

    }

    public static class MockObject {

        public String name;

        public String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public MockObject() {
        }

        public MockObject(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

}
