package com.yunsoo.api.rabbit.dto.basic;

import com.yunsoo.api.rabbit.object.ValidationResult;

import java.util.List;

/**
 * 用做和移动端交互的返回对象。
 * Created by Zhe on 2015/2/27.
 */
public class ScanResult {

    private String Key;
    private Product product;
    private Organization manufacturer;
    private List<ScanRecord> scanRecordList;
    private List<Logistics> logisticsList;
    private ValidationResult validationResult;
    private int scanCounter;
    private Long userId;

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

    public List<ScanRecord> getScanRecord() {
        return scanRecordList;
    }

    public void setScanRecord(List<ScanRecord> ScanRecordList) {
        this.scanRecordList = ScanRecordList;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
