package com.yunsoo.api.dto;

import com.yunsoo.common.data.object.LookupObject;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class ProductStatus extends LookupObject {
    public static final ProductStatus NEW = newInstance(0, "NEW");
    public static final ProductStatus ACTIVATED = newInstance(1, "ACTIVATED");
    public static final ProductStatus RECALLED = newInstance(2, "RECALLED");
    public static final ProductStatus DELETED = newInstance(3, "DELETED");


    private static ProductStatus newInstance(int id, String code) {
        ProductStatus productStatus = new ProductStatus();
        productStatus.setId(id);
        productStatus.setCode(code);
        return productStatus;
    }
}
