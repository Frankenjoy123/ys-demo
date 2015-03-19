package com.yunsoo.service;

import java.util.List;

import com.yunsoo.service.contract.ProductBase;

public interface ProductBaseService {

    public ProductBase getById(long id);

    public void save(ProductBase productBaseModel);

    public void update(ProductBase productBaseModel);

    public Boolean patchUpdate(ProductBase productBaseModel);

    public void delete(ProductBase productBaseModel);

    public Boolean delete(long id);

    public List<ProductBase> getAllProducts();

    public List<ProductBase> getProductBaseByFilter(Integer manufacturerId, Integer categoryId);
}
