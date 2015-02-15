package com.yunsoo.service.Impl;


import com.yunsoo.service.contract.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunsoo.dao.ProductDao;
import com.yunsoo.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public Product getByKey(String key) {


		return null;
	}

    @Override
    public void batchCreate() {

    }

    @Override
	public void active() {

	}
}
