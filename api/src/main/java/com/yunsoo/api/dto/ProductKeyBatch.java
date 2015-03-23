package com.yunsoo.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/16
 * Descriptions:
 */
public class ProductKeyBatch {

    private Long id;
    private Integer quantity;
    private Integer statusId;
    private Integer organizationId;
    private Integer createdClientId;
    private Long createdAccountId;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime createdDateTime;
    private List<Integer> productKeyTypeIds;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getCreatedClientId() {
        return createdClientId;
    }

    public void setCreatedClientId(Integer createdClientId) {
        this.createdClientId = createdClientId;
    }


    public Long getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(Long createdAccountId) {
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

}
