package com.yunsoo.service.Impl;

import com.yunsoo.service.ProductKeyService;
import com.yunsoo.model.ProductKey;
import com.yunsoo.dao.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhe Zhang
 */

@Service("productKeyService")
public class ProductKeyServiceImpl implements ProductKeyService {

	@Autowired
	private ProductKeyDao productkeyDao;

	@Override
	public ProductKey getById(int id) {
		return productkeyDao.getById(id);
	}

	@Override
	public void save(ProductKey productKey) {
		productkeyDao.save(productKey);
	}

	@Override
	public void update(ProductKey productKey) {
		productkeyDao.update(productKey);
	}

	@Override
	public void delete(ProductKey productKey) {
		productkeyDao.delete(productKey);
	}

	@Transactional
	@Override
	public List<ProductKey> getAllProductKeys() {
		return productkeyDao.getAllProductKeys();
	}

}