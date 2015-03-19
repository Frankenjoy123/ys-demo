package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductKeyBatchStatusModel;
import org.springframework.beans.BeanUtils;

/**
 * Created by:   Lijian
 * Created on:   2015/3/11
 * Descriptions:
 */
public class ProductKeyBatchStatus {
    private int id;
    private String code;
    private String description;
    private Boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    //util methods

    public static ProductKeyBatchStatus fromModel(ProductKeyBatchStatusModel model) {
        ProductKeyBatchStatus status = new ProductKeyBatchStatus();
        BeanUtils.copyProperties(model, status);
        return status;
    }

    public static ProductKeyBatchStatusModel toModel(ProductKeyBatchStatus status) {
        ProductKeyBatchStatusModel model = new ProductKeyBatchStatusModel();
        BeanUtils.copyProperties(status, model);
        return model;
    }
}
