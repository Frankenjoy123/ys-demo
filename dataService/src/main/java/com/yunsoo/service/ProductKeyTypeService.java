package com.yunsoo.service;

import com.yunsoo.model.ProductKeyType;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
public interface ProductKeyTypeService {

    public ProductKeyType getById(int id);

    public void save(ProductKeyType productKeyType);

    public void update(ProductKeyType productKeyType);

    public void delete(ProductKeyType productKeyType);

    public List<ProductKeyType> getAllProductKeyTypes(boolean activeOnly);
}
