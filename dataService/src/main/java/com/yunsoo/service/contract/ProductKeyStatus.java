package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductKeyStatusModel;

/**
 * Created by Zhe on 2015/1/16.
 *
 *  Convert methods for DBModel <=> Contract
 */
public class ProductKeyStatus {
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

    public static ProductKeyStatus FromModel(ProductKeyStatusModel model) {
        if (model == null) return null;
        ProductKeyStatus productKeyStatus = new ProductKeyStatus();
        productKeyStatus.setId(model.getId());
        productKeyStatus.setCode(model.getCode());
        productKeyStatus.setActive(model.isActive());
        productKeyStatus.setDescription(model.getDescription());
        return productKeyStatus;
    }

    public static ProductKeyStatusModel ToModel(ProductKeyStatus productKeyStatus) {
        if (productKeyStatus == null) return null;
        ProductKeyStatusModel model = new ProductKeyStatusModel();
        model.setId(productKeyStatus.getId());
        model.setCode(productKeyStatus.getCode());
        model.setDescription(productKeyStatus.getDescription());
        model.setActive(productKeyStatus.isActive());

        return model;
    }
}
