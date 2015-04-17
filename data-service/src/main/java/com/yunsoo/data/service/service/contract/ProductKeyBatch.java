package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.dbmodel.ProductKeyBatchModel;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/2/5
 * Descriptions:
 */
public class ProductKeyBatch {

    private Long id;
    private Integer quantity;
    private Integer statusId;
    private Integer organizationId;
    private Long productBaseId;
    private Integer createdClientId;
    private Long createdAccountId;
    private DateTime createdDateTime;
    private List<String> productKeyTypeCodes;
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

    public List<String> getProductKeyTypeCodes() {
        return productKeyTypeCodes;
    }

    public void setProductKeyTypeCodes(List<String> productKeyTypeCodes) {
        this.productKeyTypeCodes = productKeyTypeCodes;
    }


    public String getProductKeysAddress() {
        return productKeysAddress;
    }

    public void setProductKeysAddress(String productKeysAddress) {
        this.productKeysAddress = productKeysAddress;
    }


    //util methods
    public static ProductKeyBatch fromModel(ProductKeyBatchModel model) {
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(model.getId());
        batch.setQuantity(model.getQuantity());
        batch.setStatusId(model.getStatusId());
        batch.setOrganizationId(model.getOrganizationId());
        batch.setProductBaseId(model.getProductBaseId());
        batch.setCreatedClientId(model.getCreatedClientId());
        batch.setCreatedAccountId(model.getCreatedAccountId());
        batch.setCreatedDateTime(model.getCreatedDateTime());
        String codes = model.getProductKeyTypeCodes();
        if (codes != null) {
            batch.setProductKeyTypeCodes(Arrays.asList(StringUtils.delimitedListToStringArray(codes, ",")));
        }
        batch.setProductKeysAddress(model.getProductKeysAddress());

        return batch;
    }

    public static ProductKeyBatchModel toModel(ProductKeyBatch batch) {
        ProductKeyBatchModel model = new ProductKeyBatchModel();
        model.setId(batch.getId());
        model.setQuantity(batch.getQuantity());
        model.setStatusId(batch.getStatusId());
        model.setOrganizationId(batch.getOrganizationId());
        model.setProductBaseId(batch.getProductBaseId());
        model.setCreatedClientId(batch.getCreatedClientId());
        model.setCreatedAccountId(batch.getCreatedAccountId());
        model.setCreatedDateTime(batch.getCreatedDateTime());
        List<String> codes = batch.getProductKeyTypeCodes();
        if (codes != null) {
            model.setProductKeyTypeCodes(StringUtils.collectionToDelimitedString(codes, ","));
        }
        model.setProductKeysAddress(batch.getProductKeysAddress());

        return model;
    }

}
