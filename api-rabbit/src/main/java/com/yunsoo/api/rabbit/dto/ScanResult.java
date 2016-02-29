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

    @JsonProperty("manufacturer")
    private Organization manufacturer;

    @JsonProperty("followed_org")
    private Boolean followedOrg;

    @JsonProperty("liked_product")
    private Boolean likedProduct;

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

    public Boolean getLikedProduct() {
        return likedProduct;
    }

    public void setLikedProduct(Boolean likedProduct) {
        this.likedProduct = likedProduct;
    }

    public Boolean getFollowedOrg() {
        return followedOrg;
    }

    public void setFollowedOrg(Boolean followedOrg) {
        this.followedOrg = followedOrg;
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
