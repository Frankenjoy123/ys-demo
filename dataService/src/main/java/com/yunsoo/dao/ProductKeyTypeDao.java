package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductKeyTypeModel;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
public interface ProductKeyTypeDao {
    public ProductKeyTypeModel getById(int id);

    public void save(ProductKeyTypeModel productKeyTypeModel);

    public void update(ProductKeyTypeModel productKeyTypeModel);

    public void delete(ProductKeyTypeModel productKeyTypeModel);

    public List<ProductKeyTypeModel> getAllProductKeyTypes(boolean active);
}