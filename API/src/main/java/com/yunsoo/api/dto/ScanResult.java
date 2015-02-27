package com.yunsoo.api.dto;


import com.yunsoo.api.dto.basic.Logistics;
import com.yunsoo.api.dto.basic.Organization;
import com.yunsoo.api.dto.basic.Product;
import com.yunsoo.api.dto.basic.ScanRecord;

import java.util.List;

/**
 * Created by Zhe on 2015/2/27.
 */
public class ScanResult {

    private String KeyType;
    private Product product;
    private Organization manufacturer;
    private List<ScanRecord> scanRecordList;
    private List<Logistics> logisticsList;

    public String getKeyType() {
        return KeyType;
    }

    public void setKeyType(String keyType) {
        KeyType = keyType;
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
        ScanRecordList = ScanRecordList;
    }

    public List<Logistics> getLogisticsList() {
        return logisticsList;
    }

    public void setLogisticsList(List<Logistics> logisticsList) {
        this.logisticsList = logisticsList;
    }

}
