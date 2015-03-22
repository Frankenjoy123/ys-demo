package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductModel;
import org.joda.time.DateTime;

import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/2/5
 * Descriptions:
 */
public class ProductKey {

    private String productKey;
    private int productKeyTypeId;
    private boolean productKeyDisabled;
    private boolean primary;
    private Long productKeyBatchId;
    private String primaryProductKey;
    private Set<String> productKeySet;
    private DateTime createdDateTime;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public int getProductKeyTypeId() {
        return productKeyTypeId;
    }

    public void setProductKeyTypeId(int productKeyTypeId) {
        this.productKeyTypeId = productKeyTypeId;
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

    public Long getProductKeyBatchId() {
        return productKeyBatchId;
    }

    public void setProductKeyBatchId(Long productKeyBatchId) {
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

    public static ProductKey fromModel(ProductModel productModel) {
        ProductKey productKey = new ProductKey();
        productKey.setProductKey(productModel.getProductKey());
        productKey.setProductKeyTypeId(productModel.getProductKeyTypeId());
        Boolean isDisabled = productModel.getProductKeyDisabled();
        productKey.setProductKeyDisabled(isDisabled != null && isDisabled);
        productKey.setPrimary(productModel.isPrimary());
        productKey.setProductKeyBatchId(productModel.getProductKeyBatchId());
        productKey.setPrimaryProductKey(productModel.getPrimaryProductKey());
        productKey.setProductKeySet(productModel.getProductKeySet());
        productKey.setCreatedDateTime(productModel.getCreatedDateTime());
        return productKey;
    }

    public static ProductModel toModel(ProductKey productKey) {
        ProductModel productModel = new ProductModel();
        productModel.setProductKey(productKey.getProductKey());
        productModel.setProductKeyTypeId(productKey.getProductKeyTypeId());
        if (productKey.isProductKeyDisabled()) {
            productModel.setProductKeyDisabled(true);
        }
        productModel.setProductKeyBatchId(productKey.getProductKeyBatchId());
        productModel.setPrimaryProductKey(productKey.getPrimaryProductKey());
        productModel.setProductKeySet(productKey.getProductKeySet());
        productModel.setCreatedDateTime(productKey.getCreatedDateTime());
        return productModel;
    }

}
