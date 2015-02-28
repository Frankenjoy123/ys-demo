package com.yunsoo.dao;

import java.util.List;

import com.yunsoo.dbmodel.ProductBaseModel;

public interface ProductBaseDao {

    public ProductBaseModel getById(int id);

    public void save(ProductBaseModel productBaseModel);

    public void update(ProductBaseModel productBaseModel);

    public void delete(ProductBaseModel productBaseModel);

    public List<ProductBaseModel> getAllBaseProducts();

}

