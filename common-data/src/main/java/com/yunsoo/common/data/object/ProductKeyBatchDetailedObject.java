package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/8
 * Descriptions:
 */
public class ProductKeyBatchDetailedObject extends ProductKeyBatchObject implements Serializable {

    @JsonProperty("product_keys")
    private List<List<String>> productKeys;

    @JsonProperty("product_template")
    private ProductObject productTemplate;


    public List<List<String>> getProductKeys() {
        return productKeys;
    }

    public void setProductKeys(List<List<String>> productKeys) {
        this.productKeys = productKeys;
    }

    public ProductObject getProductTemplate() {
        return productTemplate;
    }

    public void setProductTemplate(ProductObject productTemplate) {
        this.productTemplate = productTemplate;
    }

}
