package com.yunsoo.service.contract;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/5
 * Descriptions:
 */
public class ProductKeyBatch {

    private String id;
    private int quantity;
    private int statusId;
    private int createdClientId;
    private int createdAccountId;
    private DateTime createdDateTime;
    private int[] productKeyTypeIds;
    private String productKeysAddress;
    private List<List<String>> productKeys;


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


    public int[] getProductKeyTypeIds() {
        return productKeyTypeIds;
    }

    public void setProductKeyTypeIds(int[] productKeyTypeIds) {
        this.productKeyTypeIds = productKeyTypeIds;
    }


    public String getProductKeysAddress() {
        return productKeysAddress;
    }

    public void setProductKeysAddress(String productKeysAddress) {
        this.productKeysAddress = productKeysAddress;
    }


    public List<List<String>> getProductKeys() {
        return productKeys;
    }

    public void setProductKeys(List<List<String>> productKeys) {
        this.productKeys = productKeys;
    }

}
