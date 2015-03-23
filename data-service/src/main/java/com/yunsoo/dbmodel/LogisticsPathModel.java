package com.yunsoo.dbmodel;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.joda.time.DateTime;

/**
 * Created by Jerry on 3/23/2015.
 */
@DynamoDBTable(tableName = "logistics_path")
public class LogisticsPathModel {
    private String productKey;
    private Integer action_id;
    private Integer startCheckPoint;
    private Long startDate;
    private Integer endCheckPoint;
    private Long endDate;
    private Long operator;
    private String desc;

    @DynamoDBHashKey(attributeName = "key")
    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    @DynamoDBRangeKey(attributeName = "start_date_dt")
    public Long getStartDateValue() {
        return startDate;
    }

    public void setStartDateValue(Long startDate) {
        this.startDate = startDate;
    }

    @DynamoDBAttribute(attributeName = "action_id")
    public Integer getAction_id() {
        return action_id;
    }

    public void setAction_id(Integer action_id) {
        this.action_id = action_id;
    }

    @DynamoDBAttribute(attributeName = "start_point")
    public Integer getStartCheckPoint() {
        return startCheckPoint;
    }

    public void setStartCheckPoint(Integer startCheckPoint) {
        this.startCheckPoint = startCheckPoint;
    }

    @DynamoDBAttribute(attributeName = "end_point")
    public Integer getEndCheckPoint() {
        return endCheckPoint;
    }

    public void setEndCheckPoint(Integer endCheckPoint) {
        this.endCheckPoint = endCheckPoint;
    }

    @DynamoDBIgnore
    public DateTime getStartDate() {
        return startDate != null ? new DateTime(startDate) : null;
    }

    public void setStartDate(DateTime startDate) {
        if (startDate != null) {
            this.startDate = startDate.getMillis();
        }
    }

    @DynamoDBIgnore
    public DateTime getEndDate() {
        return endDate != null ? new DateTime(endDate) : null;
    }

    public void setEndDate(DateTime endDate) {
        if (endDate != null) {
            this.endDate = endDate.getMillis();
        }
    }

    @DynamoDBAttribute(attributeName = "end_date_dt")
    public Long getEndDateValue() {
        return endDate;
    }

    public void setEndDateValue(Long endDate) {
        this.endDate = endDate;
    }

    @DynamoDBAttribute(attributeName = "operator")
    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
