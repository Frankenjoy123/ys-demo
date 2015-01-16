package com.yunsoo.service;

import com.yunsoo.dbmodel.ProductKeyModel;

import java.util.List;

public interface ProductKeyService {
	public ProductKeyModel getById(int id);

	public void save(ProductKeyModel productkey);

	public void update(ProductKeyModel productkey);

	public void delete(ProductKeyModel productkey);

	public List<ProductKeyModel> getAllProductKeys();
}
