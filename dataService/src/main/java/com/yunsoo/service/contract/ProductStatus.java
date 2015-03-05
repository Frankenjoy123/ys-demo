package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductStatusModel;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Zhe on 2015/1/16.
 * <p>
 * Convert methods for DBModel <=> Contract
 */
public class ProductStatus {
    private Integer Id;
    private String code;
    private String description;
    private Boolean active;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
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

    public Boolean isActive() {
        return active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static ProductStatus FromModel(ProductStatusModel model) {
        if (model == null) return null;
        ProductStatus productStatus = new ProductStatus();
        BeanUtils.copyProperties(model, productStatus);
//        productStatus.setId(model.getId());
//        productStatus.setCode(model.getCode());
//        productStatus.setActive(model.isActive());
//        productStatus.setDescription(model.getDescription());
//
        return productStatus;
    }

    public static ProductStatusModel ToModel(ProductStatus productStatus) {
        if (productStatus == null) return null;
        ProductStatusModel model = new ProductStatusModel();
        BeanUtils.copyProperties(productStatus, model);
//        model.setId(productStatus.getId());
//        model.setCode(productStatus.getCode());
//        model.setDescription(productStatus.getDescription());
//        model.setActive(productStatus.isActive());

        return model;
    }

    public static List<ProductStatus> FromModelList(List<ProductStatusModel> modelList) {
        if (modelList == null) return null;
        return modelList.stream().map(ProductStatus::FromModel).collect(Collectors.toList());
    }

    public static List<ProductStatusModel> ToModelList(List<ProductStatus> productStatusList) {
        if (productStatusList == null) return null;
        return productStatusList.stream().map(ProductStatus::ToModel).collect(Collectors.toList());
    }
}
