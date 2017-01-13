package com.yunsoo.third.dto.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yan on 7/20/2016.
 */
public class WeChatOpenIdList implements Serializable {

    @JsonProperty("total")
    private int total;
    @JsonProperty("count")
    private int count;
    @JsonProperty("data")
    private WeChatData data;

    @JsonProperty("next_openid")
    private String nextOpenId;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public WeChatData getData() {
        return data;
    }

    public void setData(WeChatData data) {
        this.data = data;
    }

    public String getNextOpenId() {
        return nextOpenId;
    }

    public void setNextOpenId(String nextOpenId) {
        this.nextOpenId = nextOpenId;
    }

    public class WeChatData {
        @JsonProperty("openid")
        private List<String> openId;

        public List<String> getOpenId() {
            return openId;
        }

        public void setOpenId(List<String> openId) {
            this.openId = openId;
        }
    }
}

