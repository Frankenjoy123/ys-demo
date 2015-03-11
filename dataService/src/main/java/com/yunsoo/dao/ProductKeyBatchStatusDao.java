package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductKeyBatchStatusModel;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/11
 * Descriptions:
 */
public interface ProductKeyBatchStatusDao {

    public List<ProductKeyBatchStatusModel> getAll(boolean activeOnly);
}
