package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.data.databind.DateTimeJsonSerializer;
import com.yunsoo.common.data.object.UserReportObject;
import org.joda.time.DateTime;

/**
 * Created by yan on 9/28/2015.
 */
public class UserReport {

    @JsonProperty("id")
    private String Id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("product_base_id")
    private String productBaseId;

    @JsonProperty("store_name")
    private String storeName;

    @JsonProperty("store_address")
    private String storeAddress;

    @JsonProperty("report_details")
    private String reportDetails;

    @JsonSerialize(using = DateTimeJsonSerializer.class)
    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    @JsonProperty("created_datetime")
    private DateTime createdDateTime;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getReportDetails() {
        return reportDetails;
    }

    public void setReportDetails(String reportDetails) {
        this.reportDetails = reportDetails;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public UserReport(){

    }

    public UserReport(UserReportObject object){
        if(object == null)
            return;
        this.createdDateTime = object.getCreatedDateTime();
        this.Id = object.getId();
        this.userId = object.getUserId();
        this.productBaseId = object.getProductBaseId();
        this.storeAddress = object.getStoreAddress();
        this.storeName = object.getStoreName();
        this.reportDetails = object.getReportDetails();
    }

    public static UserReportObject toUserReportObject(UserReport userReport){
        if(userReport == null)
            return null;
        UserReportObject object =new UserReportObject();
        object.setUserId(userReport.getUserId());
        object.setCreatedDateTime(userReport.getCreatedDateTime());
        object.setId(userReport.getId());
        object.setProductBaseId(userReport.getProductBaseId());
        object.setReportDetails(userReport.getReportDetails());
        object.setStoreAddress(userReport.getStoreAddress());
        object.setStoreName(userReport.getStoreName());
        return object;
    }


}
