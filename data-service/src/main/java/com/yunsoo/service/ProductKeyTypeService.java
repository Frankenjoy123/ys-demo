package com.yunsoo.service;

import com.yunsoo.service.contract.lookup.ProductKeyType;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/1/13
 * Descriptions:
 */
public interface ProductKeyTypeService {

    public ProductKeyType getById(int id);

    public void save(ProductKeyType productKeyTypeModel);

    public void update(ProductKeyType productKeyTypeModel);

    public void delete(ProductKeyType productKeyTypeModel);

    public List<ProductKeyType> getAllProductKeyTypes(boolean active);
}
