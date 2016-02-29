package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Dake Wang on 2016/2/22.
 */
public class ProductBatchCollection {


    @JsonProperty("product_base")
    private ProductBase productBase;

    @JsonProperty("batches")
    private List<ProductKeyBatch> batches;


    public ProductBase getProductBase() {
        return productBase;
    }

    public void setProductBase(ProductBase productBase) {
        this.productBase = productBase;
    }

    public List<ProductKeyBatch> getBatches() {
        return batches;
    }

    public void setBatches(List<ProductKeyBatch> batches) {
        this.batches = batches;
    }
}
