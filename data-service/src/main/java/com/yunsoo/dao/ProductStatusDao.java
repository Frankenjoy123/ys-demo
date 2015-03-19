package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductStatusModel;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/1/13
 * Descriptions:
 */
public interface ProductStatusDao {
    public ProductStatusModel getById(int id);

    public int save(ProductStatusModel productStatusModel);

    public DaoStatus update(ProductStatusModel productStatusModel);

    public DaoStatus patchUpdate(ProductStatusModel productStatusModelForPatch);

    public DaoStatus delete(int id);

    public void delete(ProductStatusModel productStatusModel);

    public List<ProductStatusModel> getAll(boolean active);
}
