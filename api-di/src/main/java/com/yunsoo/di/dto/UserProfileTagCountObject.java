package com.yunsoo.di.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yqy09_000 on 2016/8/26.
 */
public class UserProfileTagCountObject {

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("count")
    private int count;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
