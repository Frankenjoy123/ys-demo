package com.yunsoo.processor.controller.task;

/**
 * Created by:   Lijian
 * Created on:   2015/4/2
 * Descriptions:
 */
public class ProductKeyBatchMassage {

    private String batchId;
    private String s3Url;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }
}
