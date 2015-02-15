package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductKeyStatusModel;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
public interface ProductKeyStatusDao {
    public ProductKeyStatusModel getById(int id);

    public void save(ProductKeyStatusModel productKeyStatusModel);

    public void update(ProductKeyStatusModel productKeyStatusModel);

    public void delete(ProductKeyStatusModel productKeyStatusModel);

    public List<ProductKeyStatusModel> getAllProductKeyStatues(boolean activeOnly);
}
