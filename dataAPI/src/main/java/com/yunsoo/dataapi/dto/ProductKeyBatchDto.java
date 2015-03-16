package com.yunsoo.dataapi.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.DateTimeJsonDeserializer;
import com.yunsoo.common.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/16
 * Descriptions:
 */
public class ProductKeyBatchDto {

    private int id;
    private int quantity;
    private int statusId;
    private Integer organizationId;
    private int createdClientId;
    private int createdAccountId;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;
    private List<Integer> productKeyTypeIds;
    private String productKeysAddress;


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
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


    public List<Integer> getProductKeyTypeIds() {
        return productKeyTypeIds;
    }

    public void setProductKeyTypeIds(List<Integer> productKeyTypeIds) {
        this.productKeyTypeIds = productKeyTypeIds;
    }


    public String getProductKeysAddress() {
        return productKeysAddress;
    }

    public void setProductKeysAddress(String productKeysAddress) {
        this.productKeysAddress = productKeysAddress;
    }

}
