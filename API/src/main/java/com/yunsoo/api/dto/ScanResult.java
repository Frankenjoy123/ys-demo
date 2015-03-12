package com.yunsoo.api.dto;


import com.yunsoo.api.dto.basic.Logistics;
import com.yunsoo.api.dto.basic.Organization;
import com.yunsoo.api.dto.basic.Product;
import com.yunsoo.api.dto.basic.ScanRecord;
import com.yunsoo.api.object.ValidationResult;

import java.util.List;

/**
 * Created by Zhe on 2015/2/27.
 */
public class ScanResult {

    private String Key;
    private Product product;
    private Organization manufacturer;
    private List<ScanRecord> scanRecordList;
    private List<Logistics> logisticsList;
    private ValidationResult validationResult;

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
}
