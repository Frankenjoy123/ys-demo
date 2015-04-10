package com.yunsoo.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.api.controller.MessageController;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.LogisticsCheckActionObject;
import com.yunsoo.common.data.object.LogisticsCheckPointObject;
import com.yunsoo.common.data.object.OrganizationObject;
import org.joda.time.DateTime;

/**
 * Created by Jerry on 3/16/2015.
 */
public class LogisticsPath {
    private String productKey;

    private LogisticsCheckActionObject actionObject;

    private LogisticsCheckPointObject startCheckPointObject;
    private OrganizationObject startCheckPointOrgObject;

    private LogisticsCheckPointObject endCheckPointObject;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime startDate;
    private String desc;
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    private DateTime endDate;
    private Long operator;

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

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
