package com.yunsoo.api.dto.basic;


import com.yunsoo.api.object.ValidationResult;

import java.util.List;

/**
 * 用做和Web端交互的返回对象。
 * Created by Zhe on 2015/2/27.
 */
public class ScanResultWeb {

    private String Key;
    private Product product;
    private List<ScanRecord> scanRecordList;
    private List<Logistics> logisticses;
    private int scanCounter;
    private String message;
    //0 - 查询key不存在， 1 - ，成功， 2 - 无权查看
    private int resultCode;

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

    public int getScanCounter() {
        return scanCounter;
    }

    public void setScanCounter(int scanCounter) {
        this.scanCounter = scanCounter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
