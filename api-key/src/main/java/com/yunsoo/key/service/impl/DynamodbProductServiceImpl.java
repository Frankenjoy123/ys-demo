package com.yunsoo.key.service.impl;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.Constants;
import com.yunsoo.key.dao.dao.ProductDao;
import com.yunsoo.key.dao.model.ProductModel;
import com.yunsoo.key.dto.Product;
import com.yunsoo.key.service.ProductService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by:   Lijian
 * Created on:   2016-08-18
 * Descriptions:
 */
@Service
public class DynamodbProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getByKey(String productKey) {
        Assert.hasText(productKey, "productKey must not be null");

        ProductModel productModel = productDao.getByKey(productKey);
        if (productModel == null
                || (productModel.getKeyDisabled() != null && productModel.getKeyDisabled())) {
            return null;
        }
        String keyTypeCode = productModel.getKeyTypeCode();
        String keyBatchId = productModel.getKeyBatchId();

        if (!productModel.isPrimary() && productModel.getPrimaryKey() != null) {
            //get the primary product key
            productModel = productDao.getByKey(productModel.getPrimaryKey());
        }
        if (productModel == null) {
            return null;
        }

        Product product = new Product();
        product.setKey(productKey);
        product.setKeyTypeCode(keyTypeCode);
        product.setKeyBatchId(keyBatchId);
        product.setPrimaryKey(productModel.getKey());
        product.setKeySet(productModel.getKeySet());
        product.setCreatedDateTime(productModel.getCreatedDateTime());

        product.setProductBaseId(productModel.getProductBaseId());
        product.setStatusCode(productModel.getProductStatusCode());
        product.setManufacturingDateTime(productModel.getManufacturingDateTime());
        product.setSerialNo(productModel.getSerialNo());
        product.setDetails(productModel.getDetails());

        return product;
    }

    /**
     * update statusCode, manufacturingDateTime, details
     *
     * @param product product contains new properties
     */
    @Override
    public void patchUpdate(Product product) {
        Assert.notNull(product, "product must not be null");
        Assert.hasText(product.getKey(), "productKey must not be null or empty");

        ProductModel productModel = productDao.getByKey(product.getKey());
        if (productModel == null
                || (productModel.getKeyDisabled() != null && productModel.getKeyDisabled())) {
            throw new NotFoundException("product not found"); //product not found
        }
        if (!productModel.isPrimary()) {
            //get the primary product key
            productModel = productDao.getByKey(productModel.getPrimaryKey());
        }
        if (productModel == null) {
            throw new NotFoundException("product not found"); //product not found
        }

        String statusCode = product.getStatusCode();
        DateTime manufacturingDateTime = product.getManufacturingDateTime();
        String details = product.getDetails();

        if (statusCode != null && Constants.ProductStatus.ALL.contains(statusCode)) {
            productModel.setProductStatusCode(statusCode);
        }
        if (manufacturingDateTime != null) {
            productModel.setManufacturingDateTime(manufacturingDateTime);
        }
        if (details != null) {
            productModel.setDetails(details);
        }

        productDao.save(productModel);
    }

}
