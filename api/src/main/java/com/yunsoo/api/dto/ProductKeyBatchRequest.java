package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/16
 * Descriptions:
 */
public class ProductKeyBatchRequest {

    @Range(min = 1, max = 1000000, message = "单次申请数量必须大于1并且不超过1000000")
    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("product_key_type_codes")
    private List<String> productKeyTypeCodes;

    @JsonProperty("product_base_id")
    private String productBaseId;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<String> getProductKeyTypeCodes() {
        return productKeyTypeCodes;
    }

    public void setProductKeyTypeCodes(List<String> productKeyTypeCodes) {
        this.productKeyTypeCodes = productKeyTypeCodes;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }
}
