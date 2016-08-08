package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.MktConsumerRightObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by:  haitao
 * Created on:  2016/6/21
 * Descriptions:
 */
public class MktConsumerRight {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type_code")
    private String typeCode;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("org_id")
    private String orgId;

    @JsonProperty("status_code")
    private String statusCode;

    @JsonProperty("cmcc_flow_id")
    private Integer cmccFlowId;

    @JsonProperty("cucc_flow_id")
    private Integer cuccFlowId;

    @JsonProperty("ctcc_flow_id")
    private Integer ctccFlowId;

    @JsonProperty("store_url")
    private String storeUrl;

    @JsonProperty("created_account_id")
    private String createdAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    @JsonProperty("modified_account_id")
    private String modifiedAccountId;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("modified_datetime")
    private DateTime modifiedDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getCmccFlowId() {
        return cmccFlowId;
    }

    public void setCmccFlowId(Integer cmccFlowId) {
        this.cmccFlowId = cmccFlowId;
    }

    public Integer getCuccFlowId() {
        return cuccFlowId;
    }

    public void setCuccFlowId(Integer cuccFlowId) {
        this.cuccFlowId = cuccFlowId;
    }

    public Integer getCtccFlowId() {
        return ctccFlowId;
    }

    public void setCtccFlowId(Integer ctccFlowId) {
        this.ctccFlowId = ctccFlowId;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getModifiedAccountId() {
        return modifiedAccountId;
    }

    public void setModifiedAccountId(String modifiedAccountId) {
        this.modifiedAccountId = modifiedAccountId;
    }

    public DateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(DateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public MktConsumerRight() {
    }

    public MktConsumerRight(MktConsumerRightObject object) {
        if (object != null) {
            this.setId(object.getId());
            this.setName(object.getName());
            this.setTypeCode(object.getTypeCode());
            this.setAmount(object.getAmount());
            this.setComments(object.getComments());
            this.setOrgId(object.getOrgId());
            this.setStatusCode(object.getStatusCode());
            this.setCmccFlowId(object.getCmccFlowId());
            this.setCuccFlowId(object.getCuccFlowId());
            this.setCtccFlowId(object.getCtccFlowId());
            this.setStoreUrl(object.getStoreUrl());
            this.setCreatedAccountId(object.getCreatedAccountId());
            this.setCreatedDateTime(object.getCreatedDateTime());
            this.setModifiedAccountId(object.getModifiedAccountId());
            this.setModifiedDateTime(object.getModifiedDateTime());
        }
    }

    public MktConsumerRightObject toMktConsumerRightObject() {
        MktConsumerRightObject object = new MktConsumerRightObject();
        object.setId(this.getId());
        object.setName(this.getName());
        object.setTypeCode(this.getTypeCode());
        object.setAmount(this.getAmount());
        object.setComments(this.getComments());
        object.setOrgId(this.getOrgId());
        object.setStatusCode(this.getStatusCode());
        object.setCmccFlowId(this.getCmccFlowId());
        object.setCuccFlowId(this.getCuccFlowId());
        object.setCtccFlowId(this.getCtccFlowId());
        object.setStoreUrl(this.getStoreUrl());
        object.setCreatedAccountId(this.getCreatedAccountId());
        object.setCreatedDateTime(this.getCreatedDateTime());
        object.setModifiedAccountId(this.getModifiedAccountId());
        object.setModifiedDateTime(this.getModifiedDateTime());
        return object;
    }

}
