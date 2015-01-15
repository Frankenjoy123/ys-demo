package com.yunsoo.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunsoo.dao.BaseProductDao;
import com.yunsoo.dbmodel.BaseProduct;
import com.yunsoo.service.BaseProductService;


/**
 * @author Zhe Zhang
 */

@Service("baseProductService")
public class BaseProductServiceImpl implements BaseProductService{
	
	@Autowired
	private BaseProductDao baseProductDao;

	@Override
	public BaseProduct getById(int id) {
		return baseProductDao.getById(id);
	}

	@Override
	public void save(BaseProduct baseProduct) {
		baseProductDao.save(baseProduct);
	}

	@Override
	public void update(BaseProduct baseProduct) {
		baseProductDao.update(baseProduct);
	}

	@Override
	public void delete(BaseProduct baseProduct) {
		baseProductDao.delete(baseProduct);
	}

	@Override
	@Transactional
	public List<BaseProduct> getAllProducts() {
		return baseProductDao.getAllBaseProducts();
	}

}
