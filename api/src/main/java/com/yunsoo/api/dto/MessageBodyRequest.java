package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by  : Haitao
 * Created on  : 2015/8/12
 * Descriptions:
 */
public class MessageBodyRequest {
    @NotNull(message = "data must not be null")
    @JsonProperty("data")
    private String data; // prefix: data:text/html;base64,

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
