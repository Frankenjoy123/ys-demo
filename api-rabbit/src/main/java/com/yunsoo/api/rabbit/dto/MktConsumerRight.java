package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.common.data.object.MktConsumerRightObject;

/**
 * Created by:  haitao
 * Created on:  2016/7/1
 * Descriptions:
 */
public class MktConsumerRight {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("store_url")
    private String storeUrl;

    @JsonProperty("image_name")
    private String imageName;

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

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public MktConsumerRight() {
    }

    public MktConsumerRight(MktConsumerRightObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setName(object.getName());
            this.setTypeCode(object.getTypeCode());
            this.setAmount(object.getAmount());
            this.setComments(object.getComments());
            this.setStatusCode(object.getStatusCode());
            this.setStoreUrl(object.getStoreUrl());
            this.setImageName(object.getImageName());
        }
    }

    public MktConsumerRightObject toMktConsumerRightObject() {
        MktConsumerRightObject object = new MktConsumerRightObject();
        object.setId(this.getId());
        object.setName(this.getName());
        object.setTypeCode(this.getTypeCode());
        object.setAmount(this.getAmount());
        object.setComments(this.getComments());
        object.setStatusCode(this.getStatusCode());
        object.setStoreUrl(this.getStoreUrl());
        object.setImageName(this.getImageName());
        return object;
    }

}
