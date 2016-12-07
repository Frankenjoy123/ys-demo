package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 12/7/2016.
 */
public class WeChatJSTicket extends WeChatBaseType {

    @JsonProperty("ticket")
    private String ticket;

    @JsonProperty("expires_in")
    private Long expiresIn;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
