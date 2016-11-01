package com.yunsoo.di.dao.entity;

import com.yunsoo.di.dto.ScanRecordAnalysisObject;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Dake Wang on 2016/2/3.
 */
@Entity
@Table(name = "di_daily_scan_record")
public class ScanRecordAnalysisEntity {


    @Column(name = "scan_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Id
    private DateTime scanDate;

    @Column(name = "org_id")
    @Id
    private String orgId;

    @Column(name = "product_base_id")
    @Id
    private String productBaseId;

    @Column(name = "product_name")
    @Id
    private String productName;

    @Column(name = "batch_id")
    @Id
    private String batchId;


    @Column(name = "pv")
    private int pv;

    @Column(name = "uv")
    private int uv;

    @Column(name = "first_scan")
    private int firstScan;

    public DateTime getScanDate() {
        return scanDate;
    }

    public void setScanDate(DateTime scanDate) {
        this.scanDate = scanDate;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getFirstScan() {
        return firstScan;
    }

    public void setFirstScan(int firstScan) {
        this.firstScan = firstScan;
    }

    public static ScanRecordAnalysisObject toDataObject(ScanRecordAnalysisEntity scanRecordAnalysisEntity) {
        ScanRecordAnalysisObject object = new ScanRecordAnalysisObject();
        object.setScanDate(scanRecordAnalysisEntity.getScanDate());
        object.setBatchId(scanRecordAnalysisEntity.getBatchId());
        object.setProductBaseId(scanRecordAnalysisEntity.getProductBaseId());
        object.setPv(scanRecordAnalysisEntity.getPv());
        object.setUv(scanRecordAnalysisEntity.getUv());
        object.setFirstScan((scanRecordAnalysisEntity.getFirstScan()));
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScanRecordAnalysisEntity)) return false;

        ScanRecordAnalysisEntity entity = (ScanRecordAnalysisEntity) o;

        String key = getUniqueKey();
        return key.equals(entity.getUniqueKey());
    }

    @Override
    public int hashCode() {
        return getUniqueKey().hashCode();
    }
    public String getUniqueKey()
    {
        return scanDate.toString("yyyy-MM-dd") + orgId + productBaseId + batchId;
    }
}
