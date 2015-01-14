package com.yunsoo.service.Impl;

import com.yunsoo.dao.ProductKeyTypeDao;
import com.yunsoo.model.ProductKeyType;
import com.yunsoo.service.ProductKeyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Zhe on 2015/1/13.
 */
@Service("productKeyTypeService")
public class ProductKeyTypeServiceImpl implements ProductKeyTypeService {

    @Autowired
    private ProductKeyTypeDao productKeyTypeDao;

    @Override
    public ProductKeyType getById(int id) {
        return productKeyTypeDao.getById(id);
    }

    @Override
    public void save(ProductKeyType productKeyType){
        productKeyTypeDao.save(productKeyType);
    }

    @Override
    public void update(ProductKeyType productKeyType) {
        productKeyTypeDao.update(productKeyType);
    }

    @Override
    public void delete(ProductKeyType productKeyType) {
        productKeyTypeDao.delete(productKeyType);
    }

    @Override
    @Transactional
    public List<ProductKeyType> getAllProductKeyTypes(boolean activeOnly) {
        return productKeyTypeDao.getAllProductKeyType(activeOnly);
    }

}
