package com.yunsoo.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 用做和Web端交互的返回对象。
 * Created by Zhe on 2015/2/27.
 */
public class ScanResultWeb {

    @JsonProperty("key")
    private String Key;

    @JsonProperty("product")
    private Product product;

    @JsonProperty("scan_record_list")
    private List<ScanRecord> scanRecordList;

    @JsonProperty("logistics_list")
    private List<Logistics> logisticsList;

    @JsonProperty("scan_counter")
    private int scanCounter;

    @JsonProperty("message")
    private String message;
    //0 - 查询key不存在， 1 - ，成功， 2 - 无权查看

    @JsonProperty("result_code")
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

    public List<Logistics> getLogisticsList() {
        return logisticsList;
    }

    public void setLogisticsList(List<Logistics> logisticsList) {
        this.logisticsList = logisticsList;
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
