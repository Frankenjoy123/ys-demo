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

    private int id;
    private int quantity;
    private int statusId;
    private int organizationId;
    private int createdClientId;
    private int createdAccountId;
    private DateTime createdDateTime;
    private List<Integer> productKeyTypeIds;
    private String productKeysAddress;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public int getCreatedClientId() {
        return createdClientId;
    }

    public void setCreatedClientId(int createdClientId) {
        this.createdClientId = createdClientId;
    }


    public int getCreatedAccountId() {
        return createdAccountId;
    }

    public void setCreatedAccountId(int createdAccountId) {
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
