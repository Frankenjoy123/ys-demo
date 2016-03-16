package com.yunsoo.data.service.entity;

import com.yunsoo.common.data.object.ProductKeyBatchObject;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Arrays;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
@Entity
@Table(name = "product_key_batch")
public class ProductKeyBatchEntity {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.yunsoo.data.service.util.IdGenerator")
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

    @Column(name = "product_keys_uri")
    private String productKeysUri;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "created_app_id")
    private String createdAppId;

    @Column(name = "created_account_id")
    private String createdAccountId;

    @Column(name = "created_datetime")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    @Column(name = "rest_quantity")
    private int restQuantity;

    @Column(name = "marketing_id")
    private String marketingId;

    public int getRestQuantity() {
        return restQuantity;
    }

    public void setRestQuantity(int restQuantity) {
        this.restQuantity = restQuantity;
    }

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

    public String getProductKeysUri() {
        return productKeysUri;
    }

    public void setProductKeysUri(String productKeysUri) {
        this.productKeysUri = productKeysUri;
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

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public static ProductKeyBatchObject toDataObject(ProductKeyBatchEntity entity) {
        if (entity == null) {
            return null;
        }
        ProductKeyBatchObject batchObj = new ProductKeyBatchObject();
        batchObj.setId(entity.getId());
        batchObj.setQuantity(entity.getQuantity());
        batchObj.setStatusCode(entity.getStatusCode());
        batchObj.setOrgId(entity.getOrgId());
        batchObj.setProductBaseId(entity.getProductBaseId());
        batchObj.setCreatedAppId(entity.getCreatedAppId());
        batchObj.setCreatedAccountId(entity.getCreatedAccountId());
        batchObj.setCreatedDateTime(entity.getCreatedDateTime());
        batchObj.setRestQuantity(entity.getRestQuantity());
        String codes = entity.getProductKeyTypeCodes();
        if (codes != null) {
            batchObj.setProductKeyTypeCodes(Arrays.asList(StringUtils.delimitedListToStringArray(codes, ",")));
        }
        return batchObj;
    }

}
