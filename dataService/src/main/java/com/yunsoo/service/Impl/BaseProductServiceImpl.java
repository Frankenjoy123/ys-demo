package com.yunsoo.service.Impl;

import java.util.List;

import com.yunsoo.dbmodel.BaseProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunsoo.dao.BaseProductDao;
import com.yunsoo.service.BaseProductService;


/**
 * @author Zhe Zhang
 */

@Service("baseProductService")
public class BaseProductServiceImpl implements BaseProductService{
	
	@Autowired
	private BaseProductDao baseProductDao;

	@Override
	public BaseProductModel getById(int id) {
		return baseProductDao.getById(id);
	}

	@Override
	public void save(BaseProductModel baseProductModel) {
		baseProductDao.save(baseProductModel);
	}

	@Override
	public void update(BaseProductModel baseProductModel) {
		baseProductDao.update(baseProductModel);
	}

	@Override
	public void delete(BaseProductModel baseProductModel) {
		baseProductDao.delete(baseProductModel);
	}

	@Override
	@Transactional
	public List<BaseProductModel> getAllProducts() {
		return baseProductDao.getAllBaseProducts();
	}

}
