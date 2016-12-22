package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 11/18/2016.
 */
public class WeChatBaseType {

    @JsonProperty("errcode")
    private int errorCode;

    @JsonProperty("errmsg")
    private String errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
