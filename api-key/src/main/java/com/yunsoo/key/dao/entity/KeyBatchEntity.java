package com.yunsoo.key.dao.entity;

import com.yunsoo.key.dao.util.IdGenerator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by:   Lijian
 * Created on:   2016-08-18
 * Descriptions:
 */
@Entity
@Table(name = "product_key_batch")
public class KeyBatchEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = IdGenerator.CLASS)
    @Column(name = "id")
    private String id;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status_code")
    private String statusCode;

    @Column(name = "product_key_type_codes")
    private String productKeyTypeCodes;

    @Column(name = "product_base_id")
    private String productBaseId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "created_app_id")
    private String createdAppId;

    @Column(name = "created_device_id")
    private String createdDeviceId;

    @Column(name = "created_account_id")
    private String createdAccountId;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getProductKeyTypeCodes() {
        return productKeyTypeCodes;
    }

    public void setProductKeyTypeCodes(String productKeyTypeCodes) {
        this.productKeyTypeCodes = productKeyTypeCodes;
    }

    public String getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(String productBaseId) {
        this.productBaseId = productBaseId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreatedAppId() {
        return createdAppId;
    }

    public void setCreatedAppId(String createdAppId) {
        this.createdAppId = createdAppId;
    }

    public String getCreatedDeviceId() {
        return createdDeviceId;
    }

    public void setCreatedDeviceId(String createdDeviceId) {
        this.createdDeviceId = createdDeviceId;
    }

    public String getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(String createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
