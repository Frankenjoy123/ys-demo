package com.yunsoo.service.Impl;

import com.yunsoo.dao.ProductKeyTypeDao;
import com.yunsoo.dbmodel.ProductKeyTypeModel;
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
    public ProductKeyTypeModel getById(int id) {
        return productKeyTypeDao.getById(id);
    }

    @Override
    public void save(ProductKeyTypeModel productKeyTypeModel) {
        productKeyTypeDao.save(productKeyTypeModel);
    }

    @Override
    public void update(ProductKeyTypeModel productKeyTypeModel) {
        productKeyTypeDao.update(productKeyTypeModel);
    }

    @Override
    public void delete(ProductKeyTypeModel productKeyTypeModel) {
        productKeyTypeDao.delete(productKeyTypeModel);
    }

    @Override
    @Transactional
    public List<ProductKeyTypeModel> getAllProductKeyTypes(boolean activeOnly) {
        return productKeyTypeDao.getAllProductKeyType(activeOnly);
    }

}
