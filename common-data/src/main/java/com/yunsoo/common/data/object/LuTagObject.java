package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by:   dake
 * Created on:   2015/5/13
 * Descriptions:
 */
public class LuTagObject implements Serializable {

    @JsonProperty("id")
    private int id;

    @JsonProperty("tag_name")
    private String tagName;

    @JsonProperty("category")
    private int category;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
