package com.yunsoo.api.dto;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class ProductStatus extends StatusBase<ProductStatus> {
    public static final ProductStatus NEW = new ProductStatus(0, "NEW");
    public static final ProductStatus ACTIVATED = new ProductStatus(1, "ACTIVATED");
    public static final ProductStatus RECALLED = new ProductStatus(2, "RECALLED");
    public static final ProductStatus DELETED = new ProductStatus(3, "DELETED");

    public ProductStatus(){}

    public ProductStatus(int id, String code) {
        super(id, code);
    }
}
