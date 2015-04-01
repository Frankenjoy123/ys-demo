package com.yunsoo.service.Impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.dao.ProductBaseDao;
import com.yunsoo.dao.S3ItemDao;
import com.yunsoo.dbmodel.ProductBaseModel;
import com.yunsoo.service.contract.ProductBase;
import com.yunsoo.util.SpringBeanUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunsoo.service.ProductBaseService;


/**
 * @author Zhe Zhang
 */
@Service("productBaseService")
public class ProductBaseServiceImpl implements ProductBaseService {

    @Autowired
    private ProductBaseDao productBaseDao;
    @Autowired
    private S3ItemDao s3ItemDao;

    @Override
    public ProductBase getById(long id) {
        return ProductBase.fromModel(productBaseDao.getById(id));
    }

    @Override
    public S3Object getProductThumbnail(String bucketName, String client) throws IOException {

        try {
            S3Object item = s3ItemDao.getItem(bucketName, client);
            return item;
        } catch (AmazonS3Exception s3ex) {
            if (s3ex.getErrorCode() == "NoSuchKey") {
                //log
            }
            return null;
        } catch (Exception ex) {
            //to-do: log
            return null;
        }
    }

    @Override
    public void save(ProductBase productBase) {
        if (productBase.getCreatedDateTime() == null) {
            productBase.setCreatedDateTime(DateTime.now()); //always set createdDateTime.
        }
        productBaseDao.save(ProductBase.toModel(productBase));
    }

    @Override
    public void update(ProductBase productBase) {
        productBaseDao.update(ProductBase.toModel((productBase)));
    }

    @Override
    public void patchUpdate(ProductBase productBase) {
        productBaseDao.patchUpdate(this.getPatchModel(productBase));
    }

    @Override
    public void delete(ProductBase productBase) {
        productBaseDao.delete(ProductBase.toModel(productBase));
    }

    @Override
    public void delete(long id) {
        ProductBaseModel model = new ProductBaseModel();
        model.setId(id);
        productBaseDao.delete(model);
    }

    @Override
    public void deactivate(long id) {

    }

    @Override
    public List<ProductBase> getByFilter(Integer manufacturerId, Integer categoryId, Boolean active) {
        Map<String, Object> eqFilter = new HashMap<>();
        if (manufacturerId != null) {
            eqFilter.put("manufacturerId", manufacturerId);
        }
        if (categoryId != null) {
            eqFilter.put("categoryId", categoryId);
        }
        if (active != null) {
            eqFilter.put("active", active);
        }
        return ProductBase.fromModelList(productBaseDao.getByFilter(eqFilter));
    }

    @Override
    public List<ProductBase> getAll() {
        return ProductBase.fromModelList(productBaseDao.getAll());
    }

    //Convert Dto to Model,just copy properties which is not null.
    private ProductBaseModel getPatchModel(ProductBase productBase) {
        ProductBaseModel model = new ProductBaseModel();
        BeanUtils.copyProperties(productBase, model, SpringBeanUtil.getNullPropertyNames(productBase));
        return model;
    }
}
