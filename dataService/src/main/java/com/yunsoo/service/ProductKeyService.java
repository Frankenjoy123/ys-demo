package com.yunsoo.service;

import com.yunsoo.dbmodel.ProductKey;

import java.util.List;

public interface ProductKeyService {
	public ProductKey getById(int id);
	
	public void save(ProductKey productkey);

	public void update(ProductKey productkey);

	public void delete(ProductKey productkey);

	public List<ProductKey> getAllProductKeys();
}
