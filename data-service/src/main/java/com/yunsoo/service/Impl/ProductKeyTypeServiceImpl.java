package com.yunsoo.service.Impl;

import com.yunsoo.dao.ProductKeyTypeDao;
import com.yunsoo.service.ProductKeyTypeService;
import com.yunsoo.service.contract.ProductKeyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/1/13
 * Descriptions:
 */
@Service("productKeyTypeService")
public class ProductKeyTypeServiceImpl implements ProductKeyTypeService {

    @Autowired
    private ProductKeyTypeDao productKeyTypeDao;

    @Override
    public ProductKeyType getById(int id) {
        return ProductKeyType.fromModel(productKeyTypeDao.getById(id));
    }

    @Override
    public void save(ProductKeyType productKeyTypeModel) {
        productKeyTypeDao.save(ProductKeyType.toModel(productKeyTypeModel));
    }

    @Override
    public void update(ProductKeyType productKeyTypeModel) {
        productKeyTypeDao.update(ProductKeyType.toModel(productKeyTypeModel));
    }

    @Override
    public void delete(ProductKeyType productKeyTypeModel) {
        productKeyTypeDao.delete(ProductKeyType.toModel(productKeyTypeModel));
    }

    @Override
    @Transactional
    public List<ProductKeyType> getAllProductKeyTypes(boolean active) {
        return ProductKeyType.fromModelList(productKeyTypeDao.getAllProductKeyTypes(active));
    }

}
