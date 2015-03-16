package com.yunsoo.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Zhe on 2015/2/27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String detail;
    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
