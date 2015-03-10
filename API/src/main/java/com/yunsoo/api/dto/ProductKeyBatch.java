package com.yunsoo.api.dto;

import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2015/2/16
 * Descriptions:
 */
public class ProductKeyBatch {

    private String id;
    private int quantity;
    private int[] productKeyTypeIds;
    private int baseProductId;
    private int statusId;
    private DateTime createdDateTime;
    private String productKeysAddress;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int[] getProductKeyTypeIds() {
        return productKeyTypeIds;
    }

    public void setProductKeyTypeIds(int[] productKeyTypeIds) {
        this.productKeyTypeIds = productKeyTypeIds;
    }

    public int getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(int baseProductId) {
        this.baseProductId = baseProductId;
    }


    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }


    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }


    public String getProductKeysAddress() {
        return productKeysAddress;
    }

    public void setProductKeysAddress(String productKeysAddress) {
        this.productKeysAddress = productKeysAddress;
    }


}
