package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.ProductCategoryObject;

/**
 * Created by  : Zhe
 * Created on  : 2015/2/27
 * Descriptions:
 */
public class ProductCategory {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("active")
    private Boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ProductCategory() {
    }

    public ProductCategory(ProductCategoryObject obj) {
        if (obj != null) {
            this.setId(obj.getId());
            this.setName(obj.getName());
            this.setDescription(obj.getDescription());
            this.setParentId(obj.getParentId());
            this.setActive(obj.isActive());
        }
    }
}
