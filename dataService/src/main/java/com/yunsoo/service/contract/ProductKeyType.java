package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductKeyTypeModel;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zhe on 2015/1/16.
 * <p>
 * Convert methods for DBModel <=> Contract
 */
public class ProductKeyType {

    private Integer id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public static ProductKeyType fromModel(ProductKeyTypeModel model) {
        if (model == null) return null;
        ProductKeyType productKeyType = new ProductKeyType();
        BeanUtils.copyProperties(model, productKeyType);
        return productKeyType;
    }

    public static ProductKeyTypeModel toModel(ProductKeyType productKeyType) {
        if (productKeyType == null) return null;
        ProductKeyTypeModel model = new ProductKeyTypeModel();
        BeanUtils.copyProperties(productKeyType, model);
        return model;
    }

    public static List<ProductKeyType> fromModelList(List<ProductKeyTypeModel> modelList) {
        if (modelList == null) return null;
        return modelList.stream().map(ProductKeyType::fromModel).collect(Collectors.toList());
    }

    public static List<ProductKeyTypeModel> ToModelList(List<ProductKeyType> productKeyTypeList) {
        if (productKeyTypeList == null) return null;
        return productKeyTypeList.stream().map(ProductKeyType::toModel).collect(Collectors.toList());
    }

}
