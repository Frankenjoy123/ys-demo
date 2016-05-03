package com.yunsoo.api.rabbit.dto;

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
 * Created by Jerry on 3/16/2015.
 */
public class LogisticsPath {
    @JsonProperty("product_key")
    private String productKey;
    @JsonProperty("action_object")
    private LogisticsCheckActionObject actionObject;
    @JsonProperty("start_check_point_object")
    private LogisticsCheckPointObject startCheckPointObject;
    @JsonProperty("start_check_point_org_object")
    private OrganizationObject startCheckPointOrgObject;
    @JsonProperty("end_check_point_object")
    private LogisticsCheckPointObject endCheckPointObject;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("start_date")
    private DateTime startDate;
    @JsonProperty("desc")
    private String desc;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("end_date")
    private DateTime endDate;
    @JsonProperty("operator")
    private String operator;

    public void setActionObject(LogisticsCheckActionObject actionObject) {
        this.actionObject = actionObject;
    }

    public LogisticsCheckActionObject getActionObject() {
        return actionObject;
    }

    public void setStartCheckPointObject(LogisticsCheckPointObject startCheckPointObject) {
        this.startCheckPointObject = startCheckPointObject;
    }

    public LogisticsCheckPointObject getStartCheckPointObject() {
        return startCheckPointObject;
    }

    public void setEndCheckPointObject(LogisticsCheckPointObject endCheckPointObject) {
        this.endCheckPointObject = endCheckPointObject;
    }

    public LogisticsCheckPointObject getEndCheckPointObject() {
        return endCheckPointObject;
    }

    public void setStartCheckPointOrgObject(OrganizationObject startCheckPointOrgObject) {
        this.startCheckPointOrgObject = startCheckPointOrgObject;
    }

    public OrganizationObject getStartCheckPointOrgObject() {
        return startCheckPointOrgObject;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
