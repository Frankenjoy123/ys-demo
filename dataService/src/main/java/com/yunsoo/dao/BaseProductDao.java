package com.yunsoo.dao;

import java.util.List;

import com.yunsoo.dbmodel.BaseProduct;

public interface BaseProductDao {
	
	public BaseProduct getById(int id);
    public void save(BaseProduct baseProduct);
    public void update(BaseProduct baseProduct);
    public void delete(BaseProduct baseProduct);
    public List<BaseProduct> getAllBaseProducts();

}

