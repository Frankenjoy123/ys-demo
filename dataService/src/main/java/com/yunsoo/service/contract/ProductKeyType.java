package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductKeyTypeModel;

/**
 * Created by Zhe on 2015/1/16.
 *
 *  Convert methods for DBModel <=> Contract
 */
public class ProductKeyType {

    private int Id;
    private String code;
    private String description;
    private boolean active;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static ProductKeyType FromModel(ProductKeyTypeModel model) {
        if (model == null) return null;
        ProductKeyType productKeyType = new ProductKeyType();
        productKeyType.setId(productKeyType.getId());
        productKeyType.setCode(productKeyType.getCode());
        productKeyType.setDescription(productKeyType.getDescription());
        productKeyType.setActive(productKeyType.isActive());
        return productKeyType;
    }

    public static ProductKeyTypeModel ToModel(ProductKeyType productKeyType) {
        if (productKeyType == null) return null;
        ProductKeyTypeModel model = new ProductKeyTypeModel();
        model.setId(productKeyType.getId());
        model.setCode(productKeyType.getCode());
        model.setDescription(productKeyType.getDescription());
        model.setActive(productKeyType.isActive());
        return model;
    }

}
