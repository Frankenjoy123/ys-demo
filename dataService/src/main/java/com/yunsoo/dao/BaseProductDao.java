package com.yunsoo.dao;

import java.util.List;

import com.yunsoo.dbmodel.BaseProductModel;

public interface BaseProductDao {

    public BaseProductModel getById(int id);

    public void save(BaseProductModel baseProductModel);

    public void update(BaseProductModel baseProductModel);

    public void delete(BaseProductModel baseProductModel);

    public List<BaseProductModel> getAllBaseProducts();

}

