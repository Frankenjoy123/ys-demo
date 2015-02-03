package com.yunsoo.dao;


import com.yunsoo.dbmodel.ProductKeyModel;

public interface ProductKeyDao {



    public ProductKeyModel getByProductKey(String key);

    public void save(ProductKeyModel productKeyModel);

    public void update(ProductKeyModel productKeyModel);

}
