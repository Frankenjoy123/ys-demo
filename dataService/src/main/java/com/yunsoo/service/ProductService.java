package com.yunsoo.service;

import java.util.List;

import com.yunsoo.dbmodel.ProductModel;

public interface ProductService {

	public ProductModel getById(int id);

	public void save(ProductModel ProductModel);

	public void update(ProductModel ProductModel);

	public void delete(ProductModel ProductModel);

	public List<ProductModel> getAllProducts();
}
