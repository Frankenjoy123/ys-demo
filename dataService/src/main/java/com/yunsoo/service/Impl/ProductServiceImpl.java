package com.yunsoo.service.Impl;

import java.util.List;

import com.yunsoo.dbmodel.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yunsoo.dao.ProductDao;
import com.yunsoo.service.ProductService;

/**
 * @author Zhe Zhang
 */

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	
	@Override
	public ProductModel getById(int id) {
		return productDao.getById(id);
	}

	@Override
	public void save(ProductModel productModel) {
		productDao.save(productModel);
	}

	@Override
	public void update(ProductModel productModel) {
		productDao.update(productModel);
	}

	@Override
	public void delete(ProductModel productModel) {
		productDao.delete(productModel);
	}

	@Override
	@Transactional
	public List<ProductModel> getAllProducts() {
		return productDao.getAllProducts();
	}

}
