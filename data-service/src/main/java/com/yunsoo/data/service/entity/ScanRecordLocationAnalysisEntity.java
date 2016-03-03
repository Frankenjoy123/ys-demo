package com.yunsoo.data.service.entity;

import com.yunsoo.common.data.object.ScanRecordLocationAnalysisObject;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Dake Wang on 2016/2/3.
 */
@Entity
@Table(name = "emr_scan_record_location_analysis")
@IdClass(EMRScanLocationPK.class)
public class ScanRecordLocationAnalysisEntity {

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
    private String productName;

    @Column(name = "batch_id")
    @Id
    private String batchId;

    @Column(name = "province")
    @Id
    private String province;

    @Column(name = "city")
    @Id
    private String city;

    @Column(name = "pv")
    private int pv;


    @Column(name = "uv")
    private int uv;


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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public static ScanRecordLocationAnalysisObject toDataObject(ScanRecordLocationAnalysisEntity scanRecordLocationAnalysisEntity) {
        ScanRecordLocationAnalysisObject object = new ScanRecordLocationAnalysisObject();
        object.setPv(scanRecordLocationAnalysisEntity.getPv());
        object.setProductBaseId(scanRecordLocationAnalysisEntity.getProductBaseId());
        object.setBatchId(scanRecordLocationAnalysisEntity.getBatchId());
        object.setScanDate(scanRecordLocationAnalysisEntity.getScanDate());
        object.setProvince(scanRecordLocationAnalysisEntity.getProvince());
        object.setCity(scanRecordLocationAnalysisEntity.getCity());
        object.setUv(scanRecordLocationAnalysisEntity.getUv());
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScanRecordLocationAnalysisEntity)) return false;

        ScanRecordLocationAnalysisEntity entity = (ScanRecordLocationAnalysisEntity) o;

        String key = generateUniqueKey();
        return key.equals(entity.generateUniqueKey());
    }

    @Override
    public int hashCode() {
        return generateUniqueKey().hashCode();
    }

    public String generateUniqueKey() {
        return scanDate.toString("yyyy-MM-dd") + orgId + productBaseId + batchId + province + city;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }
}
