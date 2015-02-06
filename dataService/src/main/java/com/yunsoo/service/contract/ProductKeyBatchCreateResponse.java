package com.yunsoo.service.contract;

import org.joda.time.DateTime;

/**
 * Created by:   Lijian
 * Created on:   2015/2/4
 * Descriptions:
 */
public class ProductKeyBatchCreateResponse {

    private String batchId;

    private int quantity;

    private int[] productKeyTypeIds;

    private int statusId;

    private int createdClientId;

    private int createdAccountId;

    private DateTime createdDateTime;


    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
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

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getCreatedClientId() {
        return createdClientId;
    }

    public void setCreatedClientId(int createdClientId) {
        this.createdClientId = createdClientId;
    }

    public int getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(int createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
