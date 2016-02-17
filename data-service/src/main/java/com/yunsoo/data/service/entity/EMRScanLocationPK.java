package com.yunsoo.data.service.entity;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by Dake Wang on 2016/2/17.
 */
public class EMRScanLocationPK implements Serializable {

    private DateTime scanDate;
    private String orgId;
    private String productBaseId;
    private String batchId;
    private String province;
    private String city;


    public DateTime getScanDate() {
        return scanDate;
    }

    public void setScanDate(DateTime scanDate) {
        this.scanDate = scanDate;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
