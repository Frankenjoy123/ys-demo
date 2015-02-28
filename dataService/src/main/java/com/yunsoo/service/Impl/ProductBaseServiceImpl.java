package com.yunsoo.service.Impl;

import java.util.List;

import com.yunsoo.dao.ProductBaseDao;
import com.yunsoo.service.contract.ProductBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunsoo.service.ProductBaseService;


/**
 * @author Zhe Zhang
 */

@Service("productBaseService")
public class ProductBaseServiceImpl implements ProductBaseService {

    @Autowired
    private ProductBaseDao productBaseDao;

    @Override
    public ProductBase getById(int id) {
        return ProductBase.FromModel(productBaseDao.getById(id));
    }

    @Override
    public void save(ProductBase productBaseModel) {
        productBaseDao.save(ProductBase.ToModel(productBaseModel));
    }

    @Override
    public void update(ProductBase productBaseModel) {
        productBaseDao.update(ProductBase.ToModel((productBaseModel)));
    }

    @Override
    public void delete(ProductBase productBase) {
        productBaseDao.delete(ProductBase.ToModel(productBase));
    }

    @Override
    @Transactional
    public List<ProductBase> getAllProducts() {

        //return baseProductDao.getAllBaseProducts();
        return null;
    }

}
