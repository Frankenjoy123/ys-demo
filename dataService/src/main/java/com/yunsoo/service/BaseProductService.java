package com.yunsoo.service;

import java.util.List;

import com.yunsoo.service.contract.BaseProduct;

public interface BaseProductService {
    public BaseProduct getById(int id);

    public void save(BaseProduct baseProductModel);

    public void update(BaseProduct baseProductModel);

    public void delete(BaseProduct baseProductModel);

    public List<BaseProduct> getAllProducts();
}
