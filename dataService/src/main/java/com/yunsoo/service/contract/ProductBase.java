package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductBaseModel;

import java.util.Date;

/**
 * Created by Zhe on 2015/1/26.
 */
public class ProductBase {

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

    public static ProductBase FromModel(ProductBaseModel model) {
        if (model == null) return null;
        ProductBase productBase = new ProductBase();
        productBase.setId(model.getId());
        productBase.setSubCategoryId(model.getCategoryId());
        productBase.setManufacturerId(model.getManufacturerId());
        productBase.setName(model.getName());
        productBase.setDescription(model.getDescription());
        productBase.setCreatedDateTime(model.getCreatedDateTime());
        productBase.setBarcode(model.getBarcode());
        productBase.setDetails(model.getDetails());
        productBase.setShelfLife(model.getShelfLife());
        productBase.setShelfLifeInterval(model.getShelfLifeInterval());
        return productBase;
    }

    public static ProductBaseModel ToModel(ProductBase productBase) {
        if (productBase == null) return null;
        ProductBaseModel model = new ProductBaseModel();
        model.setId(productBase.getId());
        model.setCategoryId(productBase.getSubCategoryId());
        model.setManufacturerId(productBase.getManufacturerId());
        model.setName(productBase.getName());
        model.setDescription(productBase.getDescription());
        model.setCreatedDateTime(productBase.getCreatedDateTime());
        model.setBarcode(productBase.getBarcode());
        model.setDetails(productBase.getDetails());
        model.setShelfLife(productBase.getShelfLife());
        model.setShelfLifeInterval(productBase.getShelfLifeInterval());
        return model;
    }
}
