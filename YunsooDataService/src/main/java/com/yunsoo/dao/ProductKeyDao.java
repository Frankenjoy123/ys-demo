package com.yunsoo.dao;

import com.yunsoo.model.*;
import java.util.List;

public interface ProductKeyDao{
	public ProductKey getById(int id);
    public void save(ProductKey productKey);
    public void update(ProductKey productKey);
    public void delete(ProductKey productKey);
    public List<ProductKey> getAllProductKeys();
}
