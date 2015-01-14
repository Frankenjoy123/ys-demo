package com.yunsoo.service.Impl;

import com.yunsoo.dao.ProductKeyStatusDao;
import com.yunsoo.model.ProductKeyStatus;
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
    public ProductKeyStatus getById(int id) {
        return productKeyStatusDao.getById(id);
    }

    @Override
    public void save(ProductKeyStatus productKeyStatus) {
        productKeyStatusDao.save(productKeyStatus);
    }

    @Override
    public void update(ProductKeyStatus productKeyStatus) {
        productKeyStatusDao.update(productKeyStatus);
    }

    @Override
    public void delete(ProductKeyStatus productKeyStatus) {
        productKeyStatusDao.delete(productKeyStatus);
    }

    @Override
    @Transactional
    public List<ProductKeyStatus> getAllProductKeyStatus(boolean activeOnly) {
        return productKeyStatusDao.getAllProductKeyStatues(activeOnly);
    }
}
