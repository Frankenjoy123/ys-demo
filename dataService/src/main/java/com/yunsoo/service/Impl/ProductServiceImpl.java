package com.yunsoo.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yunsoo.dao.ProductDao;
import com.yunsoo.dbmodel.Product;
import com.yunsoo.service.ProductService;

/**
 * @author Zhe Zhang
 */

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	
	@Override
	public Product getById(int id) {
		return productDao.getById(id);
	}

	@Override
	public void save(Product product) {
		productDao.save(product);
	}

	@Override
	public void update(Product product) {
		productDao.update(product);
	}

	@Override
	public void delete(Product product) {
		productDao.delete(product);
	}

	@Override
	@Transactional
	public List<Product> getAllProducts() {
		return productDao.getAllProducts();
	}

}
