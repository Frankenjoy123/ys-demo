package com.yunsoo.common.web.client;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/29
 * Descriptions:
 */
public class ResourceInputStream extends InputStream {

    private InputStream inputStream;

    private long contentLength;

    private String contentType;


    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public ResourceInputStream(InputStream inputStream, long contentLength, String contentType) {
        this.inputStream = inputStream;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    @Override
    public int read() throws IOException {
        return inputStream != null ? inputStream.read() : 0;
    }

    public ResponseEntity<?> toResponseEntity() {
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        if (contentType != null) {
            bodyBuilder.contentType(MediaType.parseMediaType(contentType));
        }
        if (contentLength > 0) {
            bodyBuilder.contentLength(contentLength);
        }
        return bodyBuilder.body(new InputStreamResource(inputStream));
    }
}
