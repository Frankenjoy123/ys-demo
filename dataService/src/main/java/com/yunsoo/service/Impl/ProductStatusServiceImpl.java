package com.yunsoo.service.Impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.ProductStatusDao;
import com.yunsoo.dbmodel.ProductStatusModel;
import com.yunsoo.service.ProductStatusService;
import com.yunsoo.service.contract.ProductStatus;
import com.yunsoo.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;
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
    public ProductStatus getById(int id) {
        return ProductStatus.FromModel(productStatusDao.getById(id));
    }

    @Override
    public int save(ProductStatus productStatus) {
        return productStatusDao.save(ProductStatus.ToModel(productStatus));
    }

    @Override
    public Boolean patchUpdate(ProductStatus productStatus) {
        ProductStatusModel model = getPatchModel(productStatus);
        return productStatusDao.patchUpdate(model) == DaoStatus.success ? true : false;
    }

    @Override
    public Boolean update(ProductStatus productStatus) {
        ProductStatusModel model = getPatchModel(productStatus);
        return productStatusDao.update(model) == DaoStatus.success ? true : false;
    }

    @Override
    public void delete(ProductStatus productStatus) {
        productStatusDao.delete(ProductStatus.ToModel(productStatus));
    }

    @Override
    public boolean delete(int id) {
        return productStatusDao.delete(id) == DaoStatus.success;
    }

    @Override
    @Transactional
    public List<ProductStatus> getAllProductKeyStatus(boolean activeOnly) {
        return ProductStatus.FromModelList(productStatusDao.getAllProductKeyStatues(activeOnly));
    }

    //Convert Dto to Model,just copy properties which is not null.
    private ProductStatusModel getPatchModel(ProductStatus productStatus) {
        //ProductStatusModel model = productStatusDao.getById(id);
        ProductStatusModel model = new ProductStatusModel();
        BeanUtils.copyProperties(productStatus, model, SpringBeanUtil.getNullPropertyNames(productStatus));
        return model;
    }
}
