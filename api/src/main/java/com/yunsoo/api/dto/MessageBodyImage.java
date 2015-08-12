package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:  haitao
 * Created on:  2015/8/12
 * Descriptions:
 */
public class MessageBodyImage {

    @JsonProperty("path")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MessageBodyImage() {
    }

}
