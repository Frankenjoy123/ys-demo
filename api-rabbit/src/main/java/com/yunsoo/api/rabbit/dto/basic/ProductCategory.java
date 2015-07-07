package com.yunsoo.api.rabbit.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Zhe on 2015/2/27.
 */
public class ProductCategory {
    @JsonProperty("id")
    private int Id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("parent_id")
    private int parentId;
    @JsonProperty("active")
    private boolean active;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
