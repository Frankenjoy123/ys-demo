package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.LogisticsCheckActionObject;
import com.yunsoo.common.data.object.LogisticsCheckPointObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

/**
 * Created by  : Jerry
 * Created on  : 3/16/2015
 * Descriptions:
 */
public class LogisticsPath {

    @JsonProperty("product_key")
    private String productKey;

    @JsonProperty("action")
    private LogisticsCheckActionObject actionObject;

    @JsonProperty("start_point")
    private LogisticsCheckPointObject startCheckPointObject;

    @JsonProperty("end_point")
    private LogisticsCheckPointObject endCheckPointObject;

    @JsonProperty("organization")
    private OrganizationObject startCheckPointOrgObject;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("start_datetime")
    private DateTime startDateTime;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("end_datetime")
    private DateTime endDateTime;

    @JsonProperty("operator")
    private String operator;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("description")
    private String description;


    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public LogisticsCheckActionObject getActionObject() {
        return actionObject;
    }

    public void setActionObject(LogisticsCheckActionObject actionObject) {
        this.actionObject = actionObject;
    }

    public LogisticsCheckPointObject getStartCheckPointObject() {
        return startCheckPointObject;
    }

    public void setStartCheckPointObject(LogisticsCheckPointObject startCheckPointObject) {
        this.startCheckPointObject = startCheckPointObject;
    }

    public LogisticsCheckPointObject getEndCheckPointObject() {
        return endCheckPointObject;
    }

    public void setEndCheckPointObject(LogisticsCheckPointObject endCheckPointObject) {
        this.endCheckPointObject = endCheckPointObject;
    }

    public OrganizationObject getStartCheckPointOrgObject() {
        return startCheckPointOrgObject;
    }

    public void setStartCheckPointOrgObject(OrganizationObject startCheckPointOrgObject) {
        this.startCheckPointOrgObject = startCheckPointOrgObject;
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
