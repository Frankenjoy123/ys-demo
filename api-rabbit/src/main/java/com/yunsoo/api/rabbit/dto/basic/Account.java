package com.yunsoo.api.rabbit.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.api.rabbit.object.TAccountStatusEnum;

/**
 * Created by Zhe on 2015/3/3.
 */
public class Account {
    @JsonProperty("id")
    private String id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("status")
    private TAccountStatusEnum status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TAccountStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TAccountStatusEnum status) {
        this.status = status;
    }

}
