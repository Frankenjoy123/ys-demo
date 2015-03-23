package com.yunsoo.api.object;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;

/**
 * Created by Zhe on 2015/3/23.
 */
public class TMedia {
    private MediaType mediaType;
    private InputStreamResource inputStreamResource;
    private Long contentLength;

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public InputStreamResource getInputStreamResource() {
        return inputStreamResource;
    }

    public void setInputStreamResource(InputStreamResource inputStreamResource) {
        this.inputStreamResource = inputStreamResource;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }
}
