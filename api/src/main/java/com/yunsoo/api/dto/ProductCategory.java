package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.ProductCategoryObject;

/**
 * Created by  : Zhe
 * Created on  : 2015/2/27
 * Descriptions:
 */
public class ProductCategory {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("active")
    private boolean active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public ProductCategory(ProductCategoryObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setName(object.getName());
            this.setDescription(object.getDescription());
            this.setParentId(object.getParentId());
            this.setActive(object.isActive());
        }
    }
}
