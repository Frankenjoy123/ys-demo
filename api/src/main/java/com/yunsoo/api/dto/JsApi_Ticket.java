package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Admin on 6/29/2016.
 */
public class JsApi_Ticket {

    @JsonProperty("errcode")
    private Long errcode;

    @JsonProperty("errmsg")
    private String errmsg;

    @JsonProperty("ticket")
    private String ticket;

    @JsonProperty("expires_in")
    private Long expiresIn;

    public Long getErrcode() {
        return errcode;
    }

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

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