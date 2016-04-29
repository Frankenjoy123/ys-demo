package com.yunsoo.data.service.service.Impl;

import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.data.service.dao.ProductDao;
import com.yunsoo.data.service.dbmodel.dynamodb.ProductModel;
import com.yunsoo.data.service.service.ProductKeyService;
import com.yunsoo.data.service.service.contract.Product;
import com.yunsoo.data.service.service.contract.ProductKey;
import com.yunsoo.data.service.service.exception.ServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private ProductDao productDao;

    @Override
    public ProductKey get(String key) {
        Assert.notNull(key, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(key);

        return productModel != null ? new ProductKey(productModel) : null;
    }

    @Override
    public void setDisabled(String key, Boolean disabled) {
        Assert.notNull(key, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(key);
        if (productModel == null) {
            throw ServiceException.notFound("product key not found");
        }
        productModel.setProductKeyDisabled(disabled);
        productDao.save(productModel);
    }

    @Override
    public void batchSave(String productKeyBatchId,
                          List<String> productKeyTypeCodes,
                          List<List<String>> productKeys,
                          Product productTemplate) {
        Assert.notNull(productKeyBatchId, "productKeyBatchId must not be null");
        Assert.notEmpty(productKeyTypeCodes, "productKeyTypeIds must not be empty or null");
        Assert.notEmpty(productKeys, "productKeys must not be empty or null");

        if (productTemplate == null) {
            productTemplate = new Product();
        }

        DateTime startDateTime = DateTime.now();
        //generate ProductModel List
        List<ProductModel> productModels = generateProductModelList(productKeyBatchId, productKeyTypeCodes, productKeys, productTemplate);

        //save productModel
        productDao.batchSave(productModels);

        log.info("batch save product keys successfully. " + StringFormatter.formatMap(
                "productKeyBatchId", productKeyBatchId,
                "productKeyTypeCodes.size", productKeyTypeCodes.size(),
                "productKeys.size", productKeys.size(),
                "seconds", (DateTime.now().getMillis() - startDateTime.getMillis()) / 1000.0));
    }

    private List<ProductModel> generateProductModelList(String productKeyBatchId,
                                                        List<String> productKeyTypeCodes,
                                                        List<List<String>> productKeys,
                                                        Product productTemplate) {
        if (productTemplate.getCreatedDateTime() == null) {
            productTemplate.setCreatedDateTime(DateTime.now());
        }

        int productKeyTypeCodesSize = productKeyTypeCodes.size();
        List<ProductModel> productModelList = new ArrayList<>(productKeys.size() * productKeyTypeCodesSize);
        if (productKeyTypeCodesSize == 1) {
            productKeys.stream().forEach(keys -> {
                if (keys != null && keys.size() > 0) {
                    ProductModel productModel = generateProductModel(productTemplate);
                    productModel.setProductKey(keys.get(0));
                    productModel.setProductKeyTypeCode(productKeyTypeCodes.get(0));
                    productModel.setProductKeyBatchId(productKeyBatchId);
                    productModelList.add(productModel);
                }
            });
        } else { //multi keys for each product
            productKeys.stream().forEach(keys -> {
                if (keys != null && keys.size() >= productKeyTypeCodesSize) {
                    Set<String> keySet = new HashSet<>();
                    String primaryKey = keys.get(0);
                    for (int j = 0; j < productKeyTypeCodesSize; j++) {
                        String key = keys.get(j);
                        keySet.add(key);
                        ProductModel productModel = generateProductModel(productTemplate);
                        productModel.setProductKey(key);
                        productModel.setProductKeyTypeCode(productKeyTypeCodes.get(j));
                        productModel.setProductKeyBatchId(productKeyBatchId);
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
        model.setProductBaseId(productTemplate.getProductBaseId());
        model.setProductStatusCode(productTemplate.getProductStatusCode());
        model.setCreatedDateTime(productTemplate.getCreatedDateTime());
        model.setManufacturingDateTime(productTemplate.getManufacturingDateTime());
        return model;
    }
}
