package com.yunsoo.api.dto;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/16
 * Descriptions:
 */
public class ProductKeyBatchRequest {

    @Range(min = 1, max = 1000, message = "单次申请数量必须大于1并且不超过1000,[测试环境限制]")
    private int quantity;

    private List<String> productKeyTypeCodes;

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
