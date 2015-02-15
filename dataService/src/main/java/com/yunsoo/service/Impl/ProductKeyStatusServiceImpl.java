package com.yunsoo.service.Impl;

import com.yunsoo.dao.ProductKeyStatusDao;
import com.yunsoo.dbmodel.ProductKeyStatusModel;
import com.yunsoo.service.ProductKeyStatusService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
@Service("productKeyStatusService")
public class ProductKeyStatusServiceImpl implements ProductKeyStatusService {

    @Autowired
    private ProductKeyStatusDao productKeyStatusDao;

    @Override
    public ProductKeyStatusModel getById(int id) {
        return productKeyStatusDao.getById(id);
    }

    @Override
    public void save(ProductKeyStatusModel productKeyStatusModel) {
        productKeyStatusDao.save(productKeyStatusModel);
    }

    @Override
    public void update(ProductKeyStatusModel productKeyStatusModel) {
        productKeyStatusDao.update(productKeyStatusModel);
    }

    @Override
    public void delete(ProductKeyStatusModel productKeyStatusModel) {
        productKeyStatusDao.delete(productKeyStatusModel);
    }

    @Override
    @Transactional
    public List<ProductKeyStatusModel> getAllProductKeyStatus(boolean activeOnly) {
        return productKeyStatusDao.getAllProductKeyStatues(activeOnly);
    }
}
