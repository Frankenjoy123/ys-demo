package com.yunsoo.service;

import com.yunsoo.dbmodel.ProductStatusModel;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
public interface ProductStatusService {
    public ProductStatusModel getById(int id);

    public void save(ProductStatusModel productStatusModel);

    public void update(ProductStatusModel productStatusModel);

    public void delete(ProductStatusModel productStatusModel);

    public List<ProductStatusModel> getAllProductKeyStatus(boolean activeOnly);
}
