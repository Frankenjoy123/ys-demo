package com.yunsoo.api.di.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yqy09_000 on 2016/11/14.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrawAnalysisReport {

    @JsonProperty("name")
    private String drawName;

    //TODO 在标准化后应该去掉这个ID
    @JsonProperty("id")
    private String id;

    @JsonProperty("count")
    private int count;

    public String getDrawName() {
        return drawName;
    }

    public void setDrawName(String drawName) {
        this.drawName = drawName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
