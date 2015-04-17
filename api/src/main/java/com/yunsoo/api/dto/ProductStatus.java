package com.yunsoo.api.dto;

import com.yunsoo.common.data.object.LookupObject;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class ProductStatus extends LookupObject {
    public static final ProductStatus NEW = newInstance("NEW");
    public static final ProductStatus ACTIVATED = newInstance("ACTIVATED");
    public static final ProductStatus RECALLED = newInstance("RECALLED");
    public static final ProductStatus DELETED = newInstance("DELETED");


    private static ProductStatus newInstance(String code) {
        ProductStatus productStatus = new ProductStatus();
        productStatus.setCode(code);
        return productStatus;
    }
}
