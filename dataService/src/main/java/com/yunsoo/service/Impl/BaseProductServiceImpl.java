package com.yunsoo.service.Impl;

import java.util.List;

import com.yunsoo.service.contract.BaseProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunsoo.dao.BaseProductDao;
import com.yunsoo.service.BaseProductService;


/**
 * @author Zhe Zhang
 */

@Service("baseProductService")
public class BaseProductServiceImpl implements BaseProductService {

    @Autowired
    private BaseProductDao baseProductDao;

    @Override
    public BaseProduct getById(int id) {
        return BaseProduct.FromModel(baseProductDao.getById(id));
    }

    @Override
    public void save(BaseProduct baseProductModel) {
        baseProductDao.save(BaseProduct.ToModel(baseProductModel));
    }

    @Override
    public void update(BaseProduct baseProductModel) {
        baseProductDao.update(BaseProduct.ToModel((baseProductModel)));
    }

    @Override
    public void delete(BaseProduct baseProduct) {
        baseProductDao.delete(BaseProduct.ToModel(baseProduct));
    }

    @Override
    @Transactional
    public List<BaseProduct> getAllProducts() {

        //return baseProductDao.getAllBaseProducts();
        return null;
    }

}
