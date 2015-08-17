package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:  haitao
 * Created on:  2015/8/12
 * Descriptions:
 */
public class MessageBodyImage {

    @JsonProperty("imageName")
    private String imageName;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public MessageBodyImage() {
    }

}
