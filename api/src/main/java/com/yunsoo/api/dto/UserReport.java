package com.yunsoo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yunsoo.common.data.object.UserReportObject;
import com.yunsoo.common.databind.DateTimeJsonDeserializer;
import com.yunsoo.common.databind.DateTimeJsonSerializer;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by yan on 9/28/2015.
 */
public class UserReport {

    @JsonProperty("id")
    private String Id;

    @JsonProperty("user")
    private User user;

    @JsonProperty("product_base")
    private ProductBase productBase;

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

    @JsonProperty("images_name")
    public List<String> imageNames;

    public List<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(List<String> imageNames) {
        this.imageNames = imageNames;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProductBase getProductBase() {
        return productBase;
    }

    public void setProductBase(ProductBase productBase) {
        this.productBase = productBase;
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
        this.storeAddress = object.getStoreAddress();
        this.storeName = object.getStoreName();
        this.reportDetails = object.getReportDetails();
    }

    public static UserReportObject toUserReportObject(UserReport userReport){
        if(userReport == null)
            return null;
        UserReportObject object =new UserReportObject();
        object.setUserId(userReport.getUser().getId());
        object.setCreatedDateTime(userReport.getCreatedDateTime());
        object.setId(userReport.getId());
        object.setProductBaseId(userReport.getProductBase().getId());
        object.setReportDetails(userReport.getReportDetails());
        object.setStoreAddress(userReport.getStoreAddress());
        object.setStoreName(userReport.getStoreName());
        return object;
    }


}
