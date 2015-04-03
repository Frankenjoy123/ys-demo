package com.yunsoo.common.data.object;

/**
 * Created by:   Lijian
 * Created on:   2015/3/16
 * Descriptions:
 */
public class ProductKeyBatchRequestObject {

    private ProductKeyBatchObject productKeyBatch;
    private ProductObject productTemplate;

    public ProductKeyBatchObject getProductKeyBatch() {
        return productKeyBatch;
    }

    public void setProductKeyBatch(ProductKeyBatchObject productKeyBatch) {
        this.productKeyBatch = productKeyBatch;
    }

    public ProductObject getProductTemplate() {
        return productTemplate;
    }

    public void setProductTemplate(ProductObject productTemplate) {
        this.productTemplate = productTemplate;
    }

}
