package com.yunsoo.service.contract;

import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/2/5
 * Descriptions:
 */
public class ProductKey {

    private String productKey;
    private int productKeyType;
    private int statusId;
    private String batchId;
    private String primaryProductKey;
    private Set<String> productKeySet;


    public String getProductKey() {
        return productKey;
    }

    public int getProductKeyType() {
        return productKeyType;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getBatchId() {
        return batchId;
    }

    public String getPrimaryProductKey() {
        return primaryProductKey;
    }

    public Set<String> getProductKeySet() {
        return productKeySet;
    }
}
