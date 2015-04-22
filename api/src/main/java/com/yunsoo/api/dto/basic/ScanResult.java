package com.yunsoo.api.dto.basic;

import com.yunsoo.api.dto.basic.Logistics;
import com.yunsoo.api.dto.basic.Organization;
import com.yunsoo.api.dto.basic.Product;
import com.yunsoo.api.dto.basic.ScanRecord;
import com.yunsoo.api.object.ValidationResult;

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
    private List<Logistics> logisticses;
    private ValidationResult validationResult;
    private int scanCounter;
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

    public List<ScanRecord> getScanRecord() {
        return scanRecordList;
    }

    public void setScanRecord(List<ScanRecord> ScanRecordList) {
        this.scanRecordList = ScanRecordList;
    }

    public List<Logistics> getLogisticses() {
        return logisticses;
    }

    public void setLogisticses(List<Logistics> logisticses) {
        this.logisticses = logisticses;
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
