package com.yunsoo.service.Impl;

import java.util.List;

import com.yunsoo.dao.DaoStatus;
import com.yunsoo.dao.ProductBaseDao;
import com.yunsoo.dbmodel.ProductBaseModel;
import com.yunsoo.service.contract.ProductBase;
import com.yunsoo.util.SpringBeanUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
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
    public ProductBase getById(long id) {
        return ProductBase.FromModel(productBaseDao.getById(id));
    }

    @Override
    public void save(ProductBase productBaseModel) {
        productBaseModel.setCreatedDateTime(DateTime.now()); //always set createddatetime.
        productBaseDao.save(ProductBase.ToModel(productBaseModel));
    }

    @Override
    public void update(ProductBase productBaseModel) {
        productBaseDao.update(ProductBase.ToModel((productBaseModel)));
    }

    @Override
    public void delete(ProductBase productBase) {
        productBaseDao.deletePermanently(ProductBase.ToModel(productBase));
    }

    @Override
    public Boolean patchUpdate(ProductBase productBaseModel) {
        return productBaseDao.patchUpdate(this.getPatchModel(productBaseModel)) == DaoStatus.success ? true : false;
    }

    @Override
    public Boolean delete(long id) {
        return productBaseDao.delete(id) == DaoStatus.success;
    }

    @Override
    public List<ProductBase> getProductBaseByFilter(Integer manufacturerId, Integer categoryId) {
        return ProductBase.FromModelList(productBaseDao.getMessagesByFilter(manufacturerId, categoryId));
    }

    @Override
    @Transactional
    public List<ProductBase> getAllProducts() {
        return ProductBase.FromModelList(productBaseDao.getAllBaseProducts());
    }

    //Convert Dto to Model,just copy properties which is not null.
    private ProductBaseModel getPatchModel(ProductBase productBase) {
        ProductBaseModel model = new ProductBaseModel();
        BeanUtils.copyProperties(productBase, model, SpringBeanUtil.getNullPropertyNames(productBase));
        return model;
    }
}
