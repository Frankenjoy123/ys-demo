package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by admin on 2015/8/14.
 */
public class MessageToApp {

    @JsonProperty("org")
    private String org;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MessageToApp() {

    }

}
