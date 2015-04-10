package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.ProductKeyBatchModel;

import java.util.List;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductKeyBatchDao {

    public ProductKeyBatchModel getById(Long id);

    public List<ProductKeyBatchModel> getByFilterPaged(Map<String, Object> eqFilter, int pageIndex, int pageSize);

    public void save(ProductKeyBatchModel productKeyModel);

    public void update(ProductKeyBatchModel productKeyModel);
}
