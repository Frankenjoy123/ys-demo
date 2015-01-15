package com.yunsoo.service;

import java.util.List;

import com.yunsoo.dbmodel.Product;

public interface ProductService {
	
	public Product getById(int id);

	public void save(Product Product);

	public void update(Product Product);

	public void delete(Product Product);

	public List<Product> getAllProducts();
}
