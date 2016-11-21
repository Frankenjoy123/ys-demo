package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by:   Haitao
 * Created on:   2016-11-17
 * Descriptions:
 */
public class WeChatPrize {
    @JsonProperty("prize_count")
    private int prizeCount;

    @JsonProperty("wechat_prize")
    private List<MktDrawPrize> wechatPrize;

    public int getPrizeCount() {
        return prizeCount;
    }

    public void setPrizeCount(int prizeCount) {
        this.prizeCount = prizeCount;
    }

    public List<MktDrawPrize> getWechatPrize() {
        return wechatPrize;
    }

    public void setWechatPrize(List<MktDrawPrize> wechatPrize) {
        this.wechatPrize = wechatPrize;
    }
}
