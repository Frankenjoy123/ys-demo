package com.yunsoo.service.contract;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yunsoo.common.DateTimeJsonDeserializer;
import com.yunsoo.dbmodel.ProductBaseModel;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zhe on 2015/1/26.
 */
public class ProductBase {

    private Long Id;
    private Integer subCategoryId;
    private Integer manufacturerId;
    private String barcode;
    private String name;
    private String description;
    private String details;
    private int shelfLife;
    private String shelfLifeInterval;
    private DateTime createdDateTime;
    private Boolean active;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer baseProductId) {
        this.subCategoryId = baseProductId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    public void setCreatedDateTime(DateTime createdDate) {
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
        productBase.setActive(model.getActive());
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
        model.setBarcode(productBase.getBarcode());
        model.setDetails(productBase.getDetails());
        model.setShelfLife(productBase.getShelfLife());
        model.setShelfLifeInterval(productBase.getShelfLifeInterval());
        if (productBase.getCreatedDateTime() != null) {
            model.setCreatedDateTime(productBase.getCreatedDateTime()); //convert string to datetime
        }
        if (productBase.getActive() == null) {
            model.setActive(true); //default as true.
        } else {
            model.setActive(productBase.getActive());
        }
        return model;
    }

    public static List<ProductBase> FromModelList(List<ProductBaseModel> modelList) {
        if (modelList == null) return null;
        return modelList.stream().map(ProductBase::FromModel).collect(Collectors.toList());
    }

    public static List<ProductBaseModel> ToModelList(List<ProductBase> productBaseList) {
        if (productBaseList == null) return null;
        return productBaseList.stream().map(ProductBase::ToModel).collect(Collectors.toList());
    }

}
