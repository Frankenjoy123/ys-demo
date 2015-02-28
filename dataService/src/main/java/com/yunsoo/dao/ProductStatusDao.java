package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductStatusModel;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
public interface ProductStatusDao {
    public ProductStatusModel getById(int id);

    public void save(ProductStatusModel productStatusModel);

    public void update(ProductStatusModel productStatusModel);

    public void delete(ProductStatusModel productStatusModel);

    public List<ProductStatusModel> getAllProductKeyStatues(boolean activeOnly);
}
