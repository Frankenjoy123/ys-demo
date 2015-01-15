package com.yunsoo.service;

import java.util.List;

import com.yunsoo.dbmodel.BaseProduct;

public interface BaseProductService {
	public BaseProduct getById(int id);

	public void save(BaseProduct baseProduct);

	public void update(BaseProduct baseProduct);

	public void delete(BaseProduct baseProduct);

	public List<BaseProduct> getAllProducts();
}
