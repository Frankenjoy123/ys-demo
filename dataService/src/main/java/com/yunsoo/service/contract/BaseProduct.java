package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.BaseProductModel;

import java.util.Date;

/**
 * Created by Zhe on 2015/1/26.
 */
public class BaseProduct {

    private int Id;
    private int subCategoryId;
    private int manufacturerId;
    private String barcode;
    private String name;
    private String description;
    private String details;
    private int shelfLife;
    private String shelfLifeInterval;
    private Date createdDateTime;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int baseProductId) {
        this.subCategoryId = baseProductId;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getShelfLifeInterval() {
        return shelfLifeInterval;
    }

    public void setShelfLifeInterval(String shelfLifeInterval) {
        this.shelfLifeInterval = shelfLifeInterval;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDate) {
        this.createdDateTime = createdDate;
    }

    public static BaseProduct FromModel(BaseProductModel model) {
        if (model == null) return null;
        BaseProduct baseProduct = new BaseProduct();
        baseProduct.setId(model.getId());
        baseProduct.setSubCategoryId(model.getSubCategoryId());
        baseProduct.setManufacturerId(model.getManufacturerId());
        baseProduct.setName(model.getName());
        baseProduct.setDescription(model.getDescription());
        baseProduct.setCreatedDateTime(model.getCreatedDateTime());
        baseProduct.setBarcode(model.getBarcode());
        baseProduct.setDetails(model.getDetails());
        baseProduct.setShelfLife(model.getShelfLife());
        baseProduct.setShelfLifeInterval(model.getShelfLifeInterval());
        return baseProduct;
    }

    public static BaseProductModel ToModel(BaseProduct baseProduct) {
        if (baseProduct == null) return null;
        BaseProductModel model = new BaseProductModel();
        model.setId(baseProduct.getId());
        model.setSubCategoryId(baseProduct.getSubCategoryId());
        model.setManufacturerId(baseProduct.getManufacturerId());
        model.setName(baseProduct.getName());
        model.setDescription(baseProduct.getDescription());
        model.setCreatedDateTime(baseProduct.getCreatedDateTime());
        model.setBarcode(baseProduct.getBarcode());
        model.setDetails(baseProduct.getDetails());
        model.setShelfLife(baseProduct.getShelfLife());
        model.setShelfLifeInterval(baseProduct.getShelfLifeInterval());
        return model;
    }
}
