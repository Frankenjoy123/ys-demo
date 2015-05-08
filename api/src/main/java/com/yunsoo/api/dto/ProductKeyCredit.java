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

    @JsonProperty("total")
    private Long total;

    @JsonProperty("remain")
    private Long remain;

    public ProductKeyCredit() {
    }

    public ProductKeyCredit(String productBaseId) {
        this.productBaseId = productBaseId;
        this.total = 0L;
        this.remain = 0L;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getRemain() {
        return remain;
    }

    public void setRemain(Long remain) {
        this.remain = remain;
    }
}
