package com.yunsoo.service;

import java.util.List;

import com.yunsoo.service.contract.ProductBase;

public interface ProductBaseService {
    public ProductBase getById(int id);

    public void save(ProductBase productBaseModel);

    public void update(ProductBase productBaseModel);

    public void delete(ProductBase productBaseModel);

    public List<ProductBase> getAllProducts();
}
