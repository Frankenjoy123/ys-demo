package com.yunsoo.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Zhe on 2015/2/27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String productKey;
    private ProductCategory productCategory;
    private int manufacturerId;
    private String barcode;
    private String name;
    private String description;
    private String details;
    private int shelfLife;
    private String shelfLifeInterval;
    private String createdDateTime;
    private int statusId;
    private String manufacturingDateTime;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
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

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getManufacturingDateTime() {
        return manufacturingDateTime;
    }

    public void setManufacturingDateTime(String manufacturingDateTime) {
        this.manufacturingDateTime = manufacturingDateTime;
    }
}
