package com.yunsoo.service.Impl;

import com.yunsoo.dao.ProductStatusDao;
import com.yunsoo.dbmodel.ProductStatusModel;
import com.yunsoo.service.ProductStatusService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
@Service("productKeyStatusService")
public class ProductStatusServiceImpl implements ProductStatusService {

    @Autowired
    private ProductStatusDao productStatusDao;

    @Override
    public ProductStatusModel getById(int id) {
        return productStatusDao.getById(id);
    }

    @Override
    public void save(ProductStatusModel productStatusModel) {
        productStatusDao.save(productStatusModel);
    }

    @Override
    public void update(ProductStatusModel productStatusModel) {
        productStatusDao.update(productStatusModel);
    }

    @Override
    public void delete(ProductStatusModel productStatusModel) {
        productStatusDao.delete(productStatusModel);
    }

    @Override
    @Transactional
    public List<ProductStatusModel> getAllProductKeyStatus(boolean activeOnly) {
        return productStatusDao.getAllProductKeyStatues(activeOnly);
    }
}
