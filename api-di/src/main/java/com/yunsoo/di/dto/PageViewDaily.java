package com.yunsoo.di.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yqy09_000 on 2016/11/29.
 */
public class PageViewDaily {
    @JsonProperty("pv")
    private int pv;

    @JsonProperty("uv")
    private int uv;

    @JsonProperty("date")
    private String date;

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
