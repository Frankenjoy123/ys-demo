package com.yunsoo.service.Impl;

import com.yunsoo.dao.ProductDao;
import com.yunsoo.dbmodel.ProductModel;
import com.yunsoo.service.ProductKeyService;

import com.yunsoo.service.contract.ProductKey;
import com.yunsoo.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
@Service("productKeyService")
public class ProductKeyServiceImpl implements ProductKeyService {
    @Autowired
    private ProductDao productDao;

    @Override
    public ProductKey get(String key) {
        Assert.notNull(key, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(key);
        if (productModel == null) {
            return null;
        }
        return ProductKey.fromModel(productModel);
    }

    @Override
    public void disable(String key) {
        Assert.notNull(key, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(key);
        if (productModel == null) {
            throw new ServiceException(this.getClass(), "ProductKey not found");
        }
        productModel.setProductKeyDisabled(true);
        productDao.save(productModel);

    }
}
