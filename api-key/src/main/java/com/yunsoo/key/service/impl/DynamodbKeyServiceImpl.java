package com.yunsoo.key.service.impl;

import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.dao.dao.ProductDao;
import com.yunsoo.key.dao.model.ProductModel;
import com.yunsoo.key.dto.Key;
import com.yunsoo.key.dto.Product;
import com.yunsoo.key.service.KeyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2016-08-18
 * Descriptions:
 */
@Service
public class DynamodbKeyServiceImpl implements KeyService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ProductDao productDao;

    @Override
    public Key get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        ProductModel productModel = productDao.getByKey(key);
        return productModel != null ? toKey(productModel) : null;
    }

    @Override
    public void setDisabled(String key, Boolean disabled) {
        Assert.notNull(key, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(key);
        if (productModel == null) {
            throw new NotFoundException("product key not found: " + key);
        }
        productModel.setKeyDisabled(disabled);
        productDao.save(productModel);
    }

    @Override
    public void batchSave(String keyBatchId,
                          List<String> keyTypeCodes,
                          List<List<String>> productKeys,
                          Product productTemplate) {
        Assert.notNull(keyBatchId, "keyBatchId must not be null");
        Assert.notEmpty(keyTypeCodes, "keyTypeCodes must not be empty or null");
        Assert.notEmpty(productKeys, "productKeys must not be empty or null");

        if (productTemplate == null) {
            productTemplate = new Product();
        }

        DateTime startDateTime = DateTime.now();

        //generate ProductModel List
        List<ProductModel> productModels = generateProductModelList(keyBatchId, keyTypeCodes, productKeys, productTemplate);

        //save productModel
        productDao.batchSave(productModels);

        log.info("batch save product keys successfully. " + StringFormatter.formatMap(
                "keyBatchId", keyBatchId,
                "keyTypeCodes.size", keyTypeCodes.size(),
                "productKeys.size", productKeys.size(),
                "seconds", (DateTime.now().getMillis() - startDateTime.getMillis()) / 1000.0));
    }

    private List<ProductModel> generateProductModelList(String keyBatchId,
                                                        List<String> keyTypeCodes,
                                                        List<List<String>> productKeys,
                                                        Product productTemplate) {
        if (productTemplate.getCreatedDateTime() == null) {
            productTemplate.setCreatedDateTime(DateTime.now());
        }

        int keyTypeCodesSize = keyTypeCodes.size();
        List<ProductModel> productModelList = new ArrayList<>(productKeys.size() * keyTypeCodesSize);
        if (keyTypeCodesSize == 1) {
            productKeys.stream().forEach(keys -> {
                if (keys != null && keys.size() > 0) {
                    ProductModel productModel = generateProductModel(productTemplate);
                    productModel.setKey(keys.get(0));
                    productModel.setKeyTypeCode(keyTypeCodes.get(0));
                    productModel.setKeyBatchId(keyBatchId);
                    productModelList.add(productModel);
                }
            });
        } else { //multi keys for each product
            productKeys.stream().forEach(keys -> {
                if (keys != null && keys.size() >= keyTypeCodesSize) {
                    Set<String> keySet = new HashSet<>();
                    String primaryKey = keys.get(0);
                    for (int j = 0; j < keyTypeCodesSize; j++) {
                        String key = keys.get(j);
                        keySet.add(key);
                        ProductModel productModel = generateProductModel(productTemplate);
                        productModel.setKey(key);
                        productModel.setKeyTypeCode(keyTypeCodes.get(j));
                        productModel.setKeyBatchId(keyBatchId);
                        if (j == 0) {
                            productModel.setKeySet(keySet);
                        } else {
                            productModel.setPrimaryKey(primaryKey);
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
        model.setProductBaseId(productTemplate.getProductBaseId());
        model.setProductStatusCode(productTemplate.getStatusCode());
        model.setCreatedDateTime(productTemplate.getCreatedDateTime());
        model.setManufacturingDateTime(productTemplate.getManufacturingDateTime());
        return model;
    }

    private Key toKey(ProductModel productModel) {
        Key key = new Key();
        key.setKey(productModel.getKey());
        key.setTypeCode(productModel.getKeyTypeCode());
        key.setDisabled(productModel.isKeyDisabled());
        key.setPrimary(productModel.isPrimary());
        key.setBatchId(productModel.getKeyBatchId());
        key.setPrimaryKey(productModel.getPrimaryKey());
        key.setKeySet(productModel.getKeySet());
        key.setCreatedDateTime(productModel.getCreatedDateTime());
        return key;
    }

}
