package com.yunsoo.dao;

import com.yunsoo.model.ProductKeyStatus;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
public interface ProductKeyStatusDao {
    public ProductKeyStatus getById(int id);
    public void save(ProductKeyStatus productKeyStatus);
    public void update(ProductKeyStatus productKeyStatus);
    public void delete(ProductKeyStatus productKeyStatus);
    public List<ProductKeyStatus> getAllProductKeyStatues(boolean activeOnly);
}
