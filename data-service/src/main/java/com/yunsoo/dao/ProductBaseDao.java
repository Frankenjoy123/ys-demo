package com.yunsoo.dao;

import java.util.List;

import com.yunsoo.dbmodel.ProductBaseModel;

public interface ProductBaseDao {

    public ProductBaseModel getById(long id);

    public void save(ProductBaseModel productBaseModel);

    public void update(ProductBaseModel productBaseModel);

    public DaoStatus patchUpdate(ProductBaseModel productBaseModel);

    public void deletePermanently(ProductBaseModel productBaseModel);

    //Just inactive the item in database
    public DaoStatus delete(long id);

    //Search by conditions
    public List<ProductBaseModel> getProductBaseByFilter(Integer manufacturerId, Integer categoryId);

    public List<ProductBaseModel> getAllProductsBase();

}

