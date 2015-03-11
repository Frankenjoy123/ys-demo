package com.yunsoo.dbmodel;

import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by:   Lijian
 * Created on:   2015/1/30
 * Descriptions:
 */
//@Entity
//@Table(name = "product_key_batch")
public class ProductKeyBatchModel {

    private String id;
    private int quantity;
    private int statusId;
    private int organizationId;
    private int createdClientId;
    private int createdAccountId;
    private DateTime createdDateTime;
    private int[] productKeyTypeIds;
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

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
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

    public void setProductKeysAddress(String productKeySetAddress) {
        this.productKeysAddress = productKeySetAddress;
    }

}
