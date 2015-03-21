package com.yunsoo.service;

import com.yunsoo.service.contract.ProductStatus;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/1/13
 * Descriptions:
 */
public interface ProductStatusService {
    public ProductStatus getById(int id);

    public int save(ProductStatus productStatus);

    public Boolean update(ProductStatus productStatus);

    public Boolean patchUpdate(ProductStatus productStatus);

    public void delete(ProductStatus productStatus);

    public boolean delete(int id);

    public List<ProductStatus> getAllProductStatus(boolean active);
}
