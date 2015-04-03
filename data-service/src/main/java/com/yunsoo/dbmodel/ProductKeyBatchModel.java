package com.yunsoo.dbmodel;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by:   Lijian
 * Created on:   2015/1/30
 * Descriptions:
 */
@Entity
@Table(name = "product_key_batch")
public class ProductKeyBatchModel {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "product_base_id")
    private Long productBaseId;

    @Column(name = "created_client_id")
    private Integer createdClientId;

    @Column(name = "created_account_id")
    private Long createdAccountId;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "product_key_type_ids")
    private String productKeyTypeIds;

    @Column(name = "product_keys_address")
    private String productKeysAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Long getProductBaseId() {
        return productBaseId;
    }

    public void setProductBaseId(Long productBaseId) {
        this.productBaseId = productBaseId;
    }

    public Integer getCreatedClientId() {
        return createdClientId;
    }

    public void setCreatedClientId(Integer createdClientId) {
        this.createdClientId = createdClientId;
    }

    public Long getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(Long createdAccountId) {
        this.createdAccountId = createdAccountId;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getProductKeyTypeIds() {
        return productKeyTypeIds;
    }

    public void setProductKeyTypeIds(String productKeyTypeIds) {
        this.productKeyTypeIds = productKeyTypeIds;
    }

    public String getProductKeysAddress() {
        return productKeysAddress;
    }

    public void setProductKeysAddress(String productKeySetAddress) {
        this.productKeysAddress = productKeySetAddress;
    }

}
