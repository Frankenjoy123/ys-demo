package com.yunsoo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by:   Lijian
 * Created on:   2016-07-25
 * Descriptions:
 */
public class DeviceRegisterRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("os")
    private String os;

    @JsonProperty("comments")
    private String comments;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

