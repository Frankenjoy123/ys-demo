package com.yunsoo.service;

import java.util.List;

import com.yunsoo.service.contract.ProductBase;

public interface ProductBaseService {

    public ProductBase getById(long id);

    public void save(ProductBase productBase);

    public void update(ProductBase productBase);

    public void patchUpdate(ProductBase productBase);

    public void delete(ProductBase productBase);

    public void delete(long id);

    public void deactivate(long id);

    public List<ProductBase> getByFilter(Integer manufacturerId, Integer categoryId, Boolean active);

    public List<ProductBase> getAll();
}
