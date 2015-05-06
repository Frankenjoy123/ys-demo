package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/6
 * Descriptions:
 */
public class ProductKeyCredit {

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("total_credit")
    private Long totalCredit;

    @JsonProperty("remain_credit")
    private Long remainCredit;


    public ProductKeyCredit() {
    }

    public ProductKeyCredit(String productBaseId) {
        this.productBaseId = productBaseId;
        this.totalCredit = 0L;
        this.remainCredit = 0L;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public Long getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Long totalCredit) {
        this.totalCredit = totalCredit;
    }

    public Long getRemainCredit() {
        return remainCredit;
    }

    public void setRemainCredit(Long remainCredit) {
        this.remainCredit = remainCredit;
    }
}
