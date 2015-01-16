package com.yunsoo.dao;

import com.yunsoo.dbmodel.*;
import java.util.List;

public interface ProductKeyDao{
    public ProductKeyModel getById(int id);

    public void save(ProductKeyModel productKeyModel);

    public void update(ProductKeyModel productKeyModel);

    public void delete(ProductKeyModel productKeyModel);

    public List<ProductKeyModel> getAllProductKeys();
}
