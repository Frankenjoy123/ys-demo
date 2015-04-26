package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.dao.ProductDao;
import com.yunsoo.data.service.service.contract.Product;
import com.yunsoo.data.service.service.contract.ProductKey;
import com.yunsoo.data.service.service.exception.ServiceException;
import com.yunsoo.data.service.dbmodel.dynamodb.ProductModel;
import com.yunsoo.data.service.service.ProductKeyService;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public void setDisabled(String key, Boolean disable) {
        Assert.notNull(key, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(key);
        if (productModel == null) {
            throw new ServiceException(this.getClass(), "ProductKey not found");
        }
        productModel.setProductKeyDisabled(disable);
        productDao.save(productModel);
    }

    @Override
    public void batchSave(String productKeyBatchId,
                          List<String> productKeyTypeCodes,
                          List<List<String>> productKeys,
                          Product productTemplate) {
        //generate ProductModel List
        List<ProductModel> productModels
                = generateProductModelList(productKeyBatchId, productKeyTypeCodes, productKeys, productTemplate);

        //save productModel
        productDao.batchSave(productModels);

    }

    private List<ProductModel> generateProductModelList(String productKeyBatchId,
                                                        List<String> productKeyTypeCodes,
                                                        List<List<String>> productKeys,
                                                        Product productTemplate) {
        Assert.notNull(productKeyBatchId, "productKeyBatchId must not be null");
        Assert.notEmpty(productKeyTypeCodes, "productKeyTypeIds must not be empty or null");
        Assert.notEmpty(productKeys, "productKeys must not be empty or null");

        DateTime now = DateTime.now();
        List<ProductModel> productModelList = new ArrayList<>(productKeys.size() * productKeyTypeCodes.size());
        if (productKeyTypeCodes.size() == 1) {
            productKeys.stream().forEach(keys -> {
                if (keys != null && keys.size() > 0) {
                    ProductModel productModel = generateProductModel(productTemplate);
                    productModel.setProductKey(keys.get(0));
                    productModel.setProductKeyTypeCode(productKeyTypeCodes.get(0));
                    productModel.setProductKeyBatchId(productKeyBatchId);
                    productModel.setCreatedDateTime(now);
                    productModelList.add(productModel);
                }
            });
        } else { //multi keys for each product
            productKeys.stream().forEach(keys -> {
                if (keys != null && keys.size() >= productKeyTypeCodes.size()) {
                    Set<String> keySet = new HashSet<>();
                    String primaryKey = keys.get(0);
                    for (int j = 0; j < productKeyTypeCodes.size(); j++) {
                        String key = keys.get(j);
                        keySet.add(key);
                        ProductModel productModel = generateProductModel(productTemplate);
                        productModel.setProductKey(key);
                        productModel.setProductKeyTypeCode(productKeyTypeCodes.get(j));
                        productModel.setProductKeyBatchId(productKeyBatchId);
                        productModel.setCreatedDateTime(now);
                        if (j == 0) {
                            productModel.setProductKeySet(keySet);
                        } else {
                            productModel.setPrimaryProductKey(primaryKey);
                        }
                        productModelList.add(productModel);
                    }
                }
            });
        }
        return productModelList;
    }

    private ProductModel generateProductModel(Product productTemplate) {
        ProductModel model = new ProductModel();
        if (productTemplate != null) {
            model.setProductBaseId(productTemplate.getProductBaseId());
            model.setProductStatusCode(productTemplate.getProductStatusCode());
            if (productTemplate.getManufacturingDateTime() != null) {
                model.setManufacturingDateTime(productTemplate.getManufacturingDateTime());
            }
        }
        return model;
    }
}
