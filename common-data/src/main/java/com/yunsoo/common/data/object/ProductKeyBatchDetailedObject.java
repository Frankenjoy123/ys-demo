package com.yunsoo.common.data.object;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/8
 * Descriptions:
 */
public class ProductKeyBatchDetailedObject extends ProductKeyBatchObject {

    private List<List<String>> productKeys;

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
