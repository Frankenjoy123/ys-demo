package com.yunsoo.key.api.util;

import com.yunsoo.common.web.client.ResourceInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Created by:   Lijian
 * Created on:   2016-08-19
 * Descriptions:
 */
public class ResponseEntityUtils {

    private ResponseEntityUtils() {
    }

    public static ResponseEntity<?> convert(ResourceInputStream resourceInputStream) {
        String contentType = resourceInputStream.getContentType();
        long contentLength = resourceInputStream.getContentLength();

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        if (contentType != null) {
            bodyBuilder.contentType(MediaType.parseMediaType(contentType));
        }
        if (contentLength > 0) {
            bodyBuilder.contentLength(contentLength);
        }
        return bodyBuilder.body(new InputStreamResource(resourceInputStream));
    }

}
