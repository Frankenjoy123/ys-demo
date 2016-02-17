package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.dbmodel.dynamodb.ProductModel;
import org.joda.time.DateTime;

import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/2/5
 * Descriptions:
 */
public class ProductKey {

    private String productKey;

    private String productKeyTypeCode;

    private boolean productKeyDisabled;

    private boolean primary;

    private String productKeyBatchId;

    private String primaryProductKey;

    private Set<String> productKeySet;

    private DateTime createdDateTime;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductKeyTypeCode() {
        return productKeyTypeCode;
    }

    public void setProductKeyTypeCode(String productKeyTypeCode) {
        this.productKeyTypeCode = productKeyTypeCode;
    }

    public boolean isProductKeyDisabled() {
        return productKeyDisabled;
    }

    public void setProductKeyDisabled(boolean productKeyDisabled) {
        this.productKeyDisabled = productKeyDisabled;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(String productKeyBatchId) {
        this.productKeyBatchId = productKeyBatchId;
    }

    public String getPrimaryProductKey() {
        return primaryProductKey;
    }

    public void setPrimaryProductKey(String primaryProductKey) {
        this.primaryProductKey = primaryProductKey;
    }

    public Set<String> getProductKeySet() {
        return productKeySet;
    }

    public void setProductKeySet(Set<String> productKeySet) {
        this.productKeySet = productKeySet;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ProductKey() {

    }

    public ProductKey(ProductModel productModel) {
        if (productModel != null) {
            this.setProductKey(productModel.getProductKey());
            this.setProductKeyTypeCode(productModel.getProductKeyTypeCode());
            Boolean isDisabled = productModel.getProductKeyDisabled();
            this.setProductKeyDisabled(isDisabled != null && isDisabled);
            this.setPrimary(productModel.isPrimary());
            this.setProductKeyBatchId(productModel.getProductKeyBatchId());
            this.setPrimaryProductKey(productModel.getPrimaryProductKey());
            this.setProductKeySet(productModel.getProductKeySet());
            this.setCreatedDateTime(productModel.getCreatedDateTime());
        }
    }

}
