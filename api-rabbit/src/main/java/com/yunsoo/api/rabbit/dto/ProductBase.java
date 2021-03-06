package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.ProductBaseObject;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/3/19
 * Descriptions:
 */
public class ProductBase {
    @JsonProperty("id")
    private String id;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("category")
    private ProductCategory category;
    @JsonProperty("org_id")
    private String orgId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("shelf_life")
    private Integer shelfLife;
    @JsonProperty("shelf_life_interval")
    private String shelfLifeInterval;
    @JsonProperty("comments_score")
    private Long commentsScore;
    @JsonProperty("details")
    private ProductBaseDetails details;
    @JsonProperty("detail_url")
    private String detailUrl;
    @JsonProperty("following_users")
    private List<User> followingUsers;
    @JsonProperty("is_following")
    private boolean isFollowing;
    @JsonProperty("web_template_name")
    private String webTemplateName;

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public Long getCommentsScore() {
        return commentsScore;
    }

    public void setCommentsScore(Long commentsScore) {
        this.commentsScore = commentsScore;
    }

    public ProductBaseDetails getDetails() {
        return details;
    }

    public void setDetails(ProductBaseDetails details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getShelfLifeInterval() {
        return shelfLifeInterval;
    }

    public void setShelfLifeInterval(String shelfLifeInterval) {
        this.shelfLifeInterval = shelfLifeInterval;
    }

    public List<User> getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(List<User> followingUsers) {
        this.followingUsers = followingUsers;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public String getWebTemplateName() {
        return webTemplateName;
    }

    public void setWebTemplateName(String webTemplateName) {
        this.webTemplateName = webTemplateName;
    }

    public ProductBase() {
    }

    public ProductBase(ProductBaseObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setVersion(object.getVersion());
            this.setName(object.getName());
            this.setDescription(object.getDescription());
            this.setOrgId(object.getOrgId());
            this.setShelfLife(object.getShelfLife());
            this.setShelfLifeInterval(object.getOrgId());
            this.setWebTemplateName(object.getWebTemplateName());
        }
    }

    public ProductBaseObject toProductBaseObject() {
        ProductBaseObject object = new ProductBaseObject();
        object.setId(this.getId());
        object.setVersion(this.getVersion());
        object.setName(this.getName());
        object.setDescription(this.getDescription());
        object.setOrgId(this.getOrgId());
        object.setShelfLife(this.getShelfLife());
        object.setShelfLifeInterval(this.getShelfLifeInterval());
        object.setCategoryId(this.getCategory().getId());
        return object;
    }

}
