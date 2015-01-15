package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductKeyType;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
public interface ProductKeyTypeDao {
    public ProductKeyType getById(int id);
    public void save(ProductKeyType productKeyType);
    public void update(ProductKeyType productKeyType);
    public void delete(ProductKeyType productKeyType);
    public List<ProductKeyType> getAllProductKeyType(boolean activeOnly);
}