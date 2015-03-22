package com.yunsoo.service.contract;

import com.yunsoo.dbmodel.ProductKeyBatchModel;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    private Integer createdClientId;
    private Integer createdAccountId;
    private DateTime createdDateTime;
    private List<Integer> productKeyTypeIds;
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

    public Integer getCreatedClientId() {
        return createdClientId;
    }

    public void setCreatedClientId(Integer createdClientId) {
        this.createdClientId = createdClientId;
    }


    public Integer getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(Integer createdAccountId) {
        this.createdAccountId = createdAccountId;
    }


    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }


    public List<Integer> getProductKeyTypeIds() {
        return productKeyTypeIds;
    }

    public void setProductKeyTypeIds(List<Integer> productKeyTypeIds) {
        this.productKeyTypeIds = productKeyTypeIds;
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
        batch.setCreatedClientId(model.getCreatedClientId());
        batch.setCreatedAccountId(model.getCreatedAccountId());
        batch.setCreatedDateTime(model.getCreatedDateTime());
        String ids = model.getProductKeyTypeIds();
        if (ids != null) {
            batch.setProductKeyTypeIds(Arrays.stream(StringUtils.delimitedListToStringArray(ids, ","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()));
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
        model.setCreatedClientId(batch.getCreatedClientId());
        model.setCreatedAccountId(batch.getCreatedAccountId());
        model.setCreatedDateTime(batch.getCreatedDateTime());
        List<Integer> ids = batch.getProductKeyTypeIds();
        if (ids != null) {
            model.setProductKeyTypeIds(StringUtils.collectionToDelimitedString(ids, ","));
        }
        model.setProductKeysAddress(batch.getProductKeysAddress());

        return model;
    }

}
