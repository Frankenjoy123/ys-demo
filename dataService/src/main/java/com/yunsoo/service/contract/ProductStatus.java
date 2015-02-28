package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductStatusModel;

/**
 * Created by Zhe on 2015/1/16.
 * <p>
 * Convert methods for DBModel <=> Contract
 */
public class ProductStatus {
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

    public static ProductStatus FromModel(ProductStatusModel model) {
        if (model == null) return null;
        ProductStatus productStatus = new ProductStatus();
        productStatus.setId(model.getId());
        productStatus.setCode(model.getCode());
        productStatus.setActive(model.isActive());
        productStatus.setDescription(model.getDescription());
        return productStatus;
    }

    public static ProductStatusModel ToModel(ProductStatus productStatus) {
        if (productStatus == null) return null;
        ProductStatusModel model = new ProductStatusModel();
        model.setId(productStatus.getId());
        model.setCode(productStatus.getCode());
        model.setDescription(productStatus.getDescription());
        model.setActive(productStatus.isActive());

        return model;
    }
}
