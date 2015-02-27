package com.yunsoo.api.dto;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class ProductKeyStatus extends StatusBase {
    public static final ProductKeyStatus NORMAL = new ProductKeyStatus(0, "NORMAL");
    public static final ProductKeyStatus DELETED = new ProductKeyStatus(1, "DELETED");

    public ProductKeyStatus(int id, String code) {
        super(id, code);
    }
}
