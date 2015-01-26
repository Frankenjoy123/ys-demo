package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductCategoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhe on 2015/1/16.
 *
 * Convert methods for DBModel <=> Contract
 */
public class ProductCategory {
    private int Id;
    private String name;
    private String description;
    private int parentId;
    private boolean active;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    //Convert ProductCategoryModel to ProductCategory.
    public static ProductCategory FromModel(ProductCategoryModel model) {
        if (model == null) return null;
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(model.getId());
        productCategory.setName(model.getName());
        productCategory.setDescription(model.getDescription());
        productCategory.setActive(model.isActive());
        productCategory.setParentId(model.getParentId());
        return productCategory;
    }

    //Convert ProductCategory to ProductCategoryModel
    public static ProductCategoryModel ToModel(ProductCategory pc) {
        if (pc == null) return null;
        ProductCategoryModel model = new ProductCategoryModel();
        model.setId(pc.getId());
        model.setName(pc.getName());
        model.setDescription(pc.getDescription());
        model.setActive(pc.isActive());
        model.setParentId(pc.getParentId());
        return model;
    }

    //Convert List of ProductCategoryModel to List of ProductCategory.
    public static List<ProductCategory> FromModelList(List<ProductCategoryModel> modelList) {
        if (modelList == null) return null;
        List<ProductCategory> pcList = new ArrayList<ProductCategory>();
        for (ProductCategoryModel model : modelList) {
            pcList.add(com.yunsoo.service.contract.ProductCategory.FromModel(model));
        }
        return pcList;
    }

    //Convert List of ProductCategory to List of ProductCategoryModel
    public static List<ProductCategoryModel> ToModel(List<ProductCategory> pcList) {
        if (pcList == null) return null;
        List<ProductCategoryModel> modelList = new ArrayList<ProductCategoryModel>();
        for (ProductCategory pc : pcList) {
            modelList.add(com.yunsoo.service.contract.ProductCategory.ToModel(pc));
        }
        return modelList;
    }
}
