package com.yunsoo.data.service.dbmodel.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.joda.time.DateTime;

/**
 * Created by  : Jerry
 * Created on  : 3/23/2015
 * Descriptions:
 */
@DynamoDBTable(tableName = "logistics_path")
public class LogisticsPathModel {
    private String productKey;
    private String actionId;
    private String startCheckPoint;
    private Long startDateTimeValue;
    private String endCheckPoint;
    private Long endDateTimeValue;
    private String operator;
    private String deviceId;
    private String description;

    @DynamoDBHashKey(attributeName = "key")
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @DynamoDBAttribute(attributeName = "action_id")
    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    @DynamoDBAttribute(attributeName = "start_point")
    public String getStartCheckPoint() {
        return startCheckPoint;
    }

    public void setStartCheckPoint(String startCheckPoint) {
        this.startCheckPoint = startCheckPoint;
    }

    @DynamoDBIgnore
    public DateTime getStartDateTime() {
        return startDateTimeValue != null ? new DateTime(startDateTimeValue) : null;
    }

    public void setStartDateTime(DateTime startDateTime) {
        if (startDateTime != null) {
            this.startDateTimeValue = startDateTime.getMillis();
        }
    }

    @DynamoDBRangeKey(attributeName = "start_dt")
    public Long getStartDateTimeValue() {
        return startDateTimeValue;
    }

    public void setStartDateTimeValue(Long startDateTimeValue) {
        this.startDateTimeValue = startDateTimeValue;
    }

    @DynamoDBAttribute(attributeName = "end_point")
    public String getEndCheckPoint() {
        return endCheckPoint;
    }

    public void setEndCheckPoint(String endCheckPoint) {
        this.endCheckPoint = endCheckPoint;
    }

    @DynamoDBIgnore
    public DateTime getEndDateTime() {
        return endDateTimeValue != null ? new DateTime(endDateTimeValue) : null;
    }

    public void setEndDateTime(DateTime endDateTime) {
        if (endDateTime != null) {
            this.endDateTimeValue = endDateTime.getMillis();
        }
    }

    @DynamoDBAttribute(attributeName = "end_dt")
    public Long getEndDateTimeValue() {
        return endDateTimeValue;
    }

    public void setEndDateTimeValue(Long endDateTimeValue) {
        this.endDateTimeValue = endDateTimeValue;
    }

    @DynamoDBAttribute(attributeName = "operator")
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @DynamoDBAttribute(attributeName = "device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @DynamoDBAttribute(attributeName = "desc")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
