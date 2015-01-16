package com.yunsoo.service;

import com.yunsoo.dbmodel.ProductKeyStatusModel;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
public interface ProductKeyStatusService {
    public ProductKeyStatusModel getById(int id);

    public void save(ProductKeyStatusModel productKeyStatusModel);

    public void update(ProductKeyStatusModel productKeyStatusModel);

    public void delete(ProductKeyStatusModel productKeyStatusModel);

    public List<ProductKeyStatusModel> getAllProductKeyStatus(boolean activeOnly);
}
