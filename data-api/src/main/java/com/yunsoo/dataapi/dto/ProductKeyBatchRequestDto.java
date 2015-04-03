package com.yunsoo.dataapi.dto;

/**
 * Created by:   Lijian
 * Created on:   2015/3/13
 * Descriptions:
 */
public class ProductKeyBatchRequestDto {

    private ProductKeyBatchDto productKeyBatch;
    private ProductDto productTemplate;

    public ProductKeyBatchDto getProductKeyBatch() {
        return productKeyBatch;
    }

    public void setProductKeyBatch(ProductKeyBatchDto productKeyBatch) {
        this.productKeyBatch = productKeyBatch;
    }

    public ProductDto getProductTemplate() {
        return productTemplate;
    }

    public void setProductTemplate(ProductDto productTemplate) {
        this.productTemplate = productTemplate;
    }
}
