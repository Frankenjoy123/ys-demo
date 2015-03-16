package com.yunsoo.api.object;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yunsoo.common.DateTimeJsonDeserializer;
import org.joda.time.DateTime;

/**
 * Created by Zhe on 2015/3/13.
 */
public class TProductBase {
    private long Id;
    private int subCategoryId;
    private int manufacturerId;
    private String barcode;
    private String name;
    private String description;
    private String details;
    private int shelfLife;
    private String shelfLifeInterval;
    private DateTime createdDateTime;
    private Boolean active;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
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

    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
