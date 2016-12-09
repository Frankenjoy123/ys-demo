package com.yunsoo.key.dao.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.yunsoo.key.dto.ProductTrace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by:   yan
 * Created on:   10/10/2016
 * Descriptions:
 */
@DynamoDBTable(tableName = "product_trace")
public class ProductTraceModel {

    @DynamoDBHashKey(attributeName = "key")
    private String productKey;

    @DynamoDBAttribute(attributeName = "source_type")
    private List<String> sourceTypeList;

    @DynamoDBAttribute(attributeName = "source_id")
    private List<String> sourceIdList;

    @DynamoDBAttribute(attributeName = "created_source_id")
    private List<String> createdSourceIdList;

    @DynamoDBAttribute(attributeName = "created_source_type")
    private List<String> createdSourceTypeList;

    @DynamoDBAttribute(attributeName = "action")
    private List<String> actionList;

    @DynamoDBAttribute(attributeName = "date_time")
    private List<Long> dateTimeList;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public List<Long> getDateTimeList() {
        return dateTimeList;
    }

    public void setDateTimeList(List<Long> dateTimeList) {
        this.dateTimeList = dateTimeList;
    }

    public List<String> getActionList() {
        return actionList;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }

    public List<String> getSourceIdList() {
        return sourceIdList;
    }

    public void setSourceIdList(List<String> sourceIdList) {
        this.sourceIdList = sourceIdList;
    }

    public List<String> getSourceTypeList() {
        return sourceTypeList;
    }

    public void setSourceTypeList(List<String> sourceTypeList) {
        this.sourceTypeList = sourceTypeList;
    }

    public List<String> getCreatedSourceIdList() {
        return createdSourceIdList;
    }

    public void setCreatedSourceIdList(List<String> createdSourceIdList) {
        this.createdSourceIdList = createdSourceIdList;
    }

    public List<String> getCreatedSourceTypeList() {
        return createdSourceTypeList;
    }

    public void setCreatedSourceTypeList(List<String> createdSourceTypeList) {
        this.createdSourceTypeList = createdSourceTypeList;
    }

    public ProductTraceModel(){}

    public ProductTraceModel(ProductTrace trace){
        this.setProductKey(trace.getProductKey());
        this.setActionList(new ArrayList<>(Arrays.asList(trace.getAction())));
        this.setDateTimeList(new ArrayList<>(Arrays.asList(trace.getCreatedDateTime().getMillis())));
        this.setSourceIdList(new ArrayList<>(Arrays.asList(trace.getSourceId())));
        this.setSourceTypeList(new ArrayList<>(Arrays.asList(trace.getSourceType())));
        this.setCreatedSourceIdList(new ArrayList<>(Arrays.asList(trace.getCreatedSourceId())));
        this.setCreatedSourceTypeList(new ArrayList<>(Arrays.asList(trace.getCreatedSourceType())));
    }
}
