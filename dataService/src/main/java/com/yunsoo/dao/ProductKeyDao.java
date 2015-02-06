package com.yunsoo.dao;


import com.yunsoo.dbmodel.ProductKeyModel;

import java.util.List;

public interface ProductKeyDao {

    public ProductKeyModel getByProductKey(String key);

    public void save(ProductKeyModel productKeyModel);

    public void batchSave(List<ProductKeyModel> productKeyModels);

    public void update(ProductKeyModel productKeyModel);

}
