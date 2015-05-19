package com.yunsoo.api.rabbit.object;

/**
 * Created by KB on 2015/2/27.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TAccountToken {
    private Long id;
    private Integer status;
    private TAccount account;
    private Long deviceId;
    private String accessToken;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public TAccount getAccount() {
        return account;
    }

    public void setAccount(TAccount account) {
        this.account = account;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long device) {
        this.deviceId = deviceId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
