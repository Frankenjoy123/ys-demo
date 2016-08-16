package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by  : Zhe
 * Created on  : 2015/2/27
 * Descriptions:
 */
public class Organization extends com.yunsoo.api.rabbit.auth.dto.Organization {

    @JsonProperty("products")
    private List<ProductBase> productBaseList;

    public List<ProductBase> getProductBaseList() {
        return productBaseList;
    }

    public void setProductBaseList(List<ProductBase> productBaseList) {
        this.productBaseList = productBaseList;
    }

    public Organization() {
    }

    public Organization(com.yunsoo.api.rabbit.auth.dto.Organization org) {
        if (org != null) {
            this.setId(org.getId());
            this.setName(org.getName());
            this.setStatusCode(org.getStatusCode());
            this.setDescription(org.getDescription());
            this.setTypeCode(org.getTypeCode());
            this.setCreatedDateTime(org.getCreatedDateTime());
        }
    }

}
