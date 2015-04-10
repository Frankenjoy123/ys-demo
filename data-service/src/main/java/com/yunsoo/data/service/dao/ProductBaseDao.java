package com.yunsoo.data.service.dao;

import java.util.List;
import java.util.Map;

import com.yunsoo.data.service.dbmodel.ProductBaseModel;

public interface ProductBaseDao {

    public ProductBaseModel getById(long id);

    public void save(ProductBaseModel productBaseModel);

    public void update(ProductBaseModel productBaseModel);

    public void patchUpdate(ProductBaseModel productBaseModelForPatch);

    public void delete(ProductBaseModel productBaseModel);

    //Search by conditions
    public List<ProductBaseModel> getByFilter(Map<String, Object> eqFilter);

    public List<ProductBaseModel> getAll();

}

