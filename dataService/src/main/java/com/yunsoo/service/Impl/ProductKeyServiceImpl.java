package com.yunsoo.service.Impl;

import com.yunsoo.dbmodel.ProductKeyModel;
import com.yunsoo.service.ProductKeyService;
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
	public ProductKeyModel getById(int id) {
		return productkeyDao.getById(id);
	}

	@Override
	public void save(ProductKeyModel productKeyModel) {
		productkeyDao.save(productKeyModel);
	}

	@Override
	public void update(ProductKeyModel productKeyModel) {
		productkeyDao.update(productKeyModel);
	}

	@Override
	public void delete(ProductKeyModel productKeyModel) {
		productkeyDao.delete(productKeyModel);
	}

	@Transactional
	@Override
	public List<ProductKeyModel> getAllProductKeys() {
		return productkeyDao.getAllProductKeys();
	}

}