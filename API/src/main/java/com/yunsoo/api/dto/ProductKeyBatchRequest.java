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

    @Range(min = 1, max = 10, message = "单次申请数量必须大于1并且不超过10") //10 for test only, this should be 100000
    private int quantity;

    @NotEmpty(message = "必须指定一个或多个产品码类型")
    private List<String> productKeyTypeCodes;

    private Integer productBaseId;

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

    public Integer getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(Integer productBaseId) {
        this.productBaseId = productBaseId;
    }
}
