package com.yunsoo.api.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yunsoo.api.rabbit.object.ValidationResult;

import java.util.List;

/**
 * 用做和移动端交互的返回对象。
 * Created by Zhe on 2015/2/27.
 */
public class ScanResult {
    @JsonProperty("key")
    private String Key;
    @JsonProperty("product")
    private Product product;
    @JsonProperty("liked_product")
    private Boolean liked_product;
    @JsonProperty("manufacturer")
    private Organization manufacturer;
    @JsonProperty("followed_org")
    private Boolean followed_org;
    @JsonProperty("scan_record_list")
    private List<ScanRecord> scanRecordList;
    @JsonProperty("logistics_list")
    private List<Logistics> logisticsList;
    @JsonProperty("validation_result")
    private ValidationResult validationResult;
    @JsonProperty("scan_counter")
    private int scanCounter;
    @JsonProperty("user_id")
    private String userId;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Organization getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Organization manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Boolean getLiked_product() {
        return liked_product;
    }

    public void setLiked_product(Boolean liked_product) {
        this.liked_product = liked_product;
    }

    public Boolean getFollowed_org() {
        return followed_org;
    }

    public void setFollowed_org(Boolean followed_org) {
        this.followed_org = followed_org;
    }

    public List<ScanRecord> getScanRecordList() {
        return scanRecordList;
    }

    public void setScanRecordList(List<ScanRecord> scanRecordList) {
        this.scanRecordList = scanRecordList;
    }

    public List<Logistics> getLogisticsList() {
        return logisticsList;
    }

    public void setLogisticsList(List<Logistics> logisticsList) {
        this.logisticsList = logisticsList;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public int getScanCounter() {
        return scanCounter;
    }

    public void setScanCounter(int scanCounter) {
        this.scanCounter = scanCounter;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
