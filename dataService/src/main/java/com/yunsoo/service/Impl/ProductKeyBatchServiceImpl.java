package com.yunsoo.service.Impl;

import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.dao.ProductDao;
import com.yunsoo.dao.ProductKeyBatchDao;
import com.yunsoo.dao.S3ItemDao;
import com.yunsoo.dbmodel.ProductKeyBatchModel;
import com.yunsoo.dbmodel.ProductKeyBatchS3ObjectModel;
import com.yunsoo.dbmodel.ProductModel;
import com.yunsoo.service.ProductKeyBatchService;
import com.yunsoo.service.contract.Product;
import com.yunsoo.service.contract.ProductKeyBatch;
import com.yunsoo.util.KeyGenerator;
import com.yunsoo.util.YunsooConfig;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
@Service("productKeyBatchService")
public class ProductKeyBatchServiceImpl implements ProductKeyBatchService {

    @Autowired
    private ProductKeyBatchDao productkeyBatchDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private S3ItemDao s3ItemDao;

    @Override
    public ProductKeyBatch getById(int batchId) {
        ProductKeyBatchModel model = productkeyBatchDao.getById(batchId);
        return model == null ? null : ProductKeyBatch.fromModel(model);
    }

    @Override
    public List<List<String>> getProductKeys(int batchId) {
        ProductKeyBatch keyBatch = this.getById(batchId);
        if (keyBatch == null) {
            return null;
        }

        String address = keyBatch.getProductKeysAddress();
        ProductKeyBatchS3ObjectModel model = getProductKeyListFromS3(address);
        return model == null ? null : model.getProductKeys();
    }

    @Override
    public ProductKeyBatch create(ProductKeyBatch batch) {
        return createWithProduct(batch, null);
    }

    @Override
    public ProductKeyBatch createWithProduct(ProductKeyBatch batch, Product productTemplate) {
        int quantity = batch.getQuantity();
        int[] keyTypeIds = batch.getProductKeyTypeIds();

        Assert.isTrue(quantity > 0, "quantity must be greater than 0");
        Assert.isTrue(keyTypeIds.length > 0, "productKeyTypeIds must not be empty");

        //
        List<List<String>> keyList = generateProductKeys(batch);

        //save keyList to S3
        String address = saveProductKeyListToS3(batch, keyList);

        //generate ProductModel List
        List<ProductModel> productModel = generateProductModelList(productTemplate, quantity, keyTypeIds, keyList);

        //save productModel
        productDao.batchSave(productModel);

        //save batch
        batch.setId(0); //set to 0 for creating new item
        batch.setProductKeysAddress(address);
        return saveProductKeyBatch(batch);
    }

    @Override
    public ProductKeyBatch createAsync(ProductKeyBatch batch) {
        return createWithProductAsync(batch, null);
    }

    @Override
    public ProductKeyBatch createWithProductAsync(ProductKeyBatch batch, Product productTemplate) {

        throw new NotImplementedException();
    }

    //private methods

    private List<List<String>> generateProductKeys(ProductKeyBatch batch) {
        int quantity = batch.getQuantity();
        int[] keyTypeIds = batch.getProductKeyTypeIds();

        List<List<String>> keyList = new ArrayList<>(quantity);

        for (int i = 0, len = keyTypeIds.length; i < quantity; i++) {
            List<String> tempKeys = new ArrayList<>(len);
            for (int j = 0; j < len; j++) {
                tempKeys.add(KeyGenerator.newKey());
            }
            keyList.add(tempKeys);
        }

        return keyList;
    }

    private List<ProductModel> generateProductModelList(Product productTemplate, int quantity, int[] keyTypeIds, List<List<String>> keyList) {
        Assert.isTrue(quantity > 0 && quantity == keyList.size(), "keyList not valid");
        List<ProductModel> productModelList = new ArrayList<>(quantity * keyTypeIds.length);
        if (keyTypeIds.length == 1) {
            keyList.stream().forEach(keys -> {
                if (keys != null && keys.size() > 0) {
                    ProductModel productModel = generateProductModel(productTemplate);
                    productModel.setProductKey(keys.get(0));
                    productModel.setProductKeyTypeId(keyTypeIds[0]);
                    productModelList.add(productModel);
                }
            });
        } else { //multi keys for each product
            keyList.stream().forEach(keys -> {
                if (keys != null && keys.size() >= keyTypeIds.length) {
                    Set<String> keySet = new HashSet<>();
                    String primaryKey = keys.get(0);
                    for (int j = 0; j < keyTypeIds.length; j++) {
                        String key = keys.get(j);
                        ProductModel productModel = generateProductModel(productTemplate);
                        productModel.setProductKey(key);
                        productModel.setProductKeyTypeId(keyTypeIds[j]);
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
            model.setProductStatusId(productTemplate.getProductStatusId());
            if (productTemplate.getManufacturingDateTime() != null) {
                model.setManufacturingDateTime(productTemplate.getManufacturingDateTime());
            }
            model.setCreatedDateTime(productTemplate.getCreatedDateTime() == null
                    ? DateTime.now()
                    : productTemplate.getCreatedDateTime());
        }
        return model;
    }

    private String saveProductKeyListToS3(ProductKeyBatch batch, List<List<String>> keyList) {
        ProductKeyBatchS3ObjectModel model = new ProductKeyBatchS3ObjectModel();
        model.setId(batch.getId());
        model.setQuantity(batch.getQuantity());
        model.setCreatedDateTimeStr(DateTimeUtils.toString(batch.getCreatedDateTime()));
        model.setProductKeyTypeIds(batch.getProductKeyTypeIds());
        model.setProductKeys(keyList);
        String bucketName = YunsooConfig.getProductKeyBatchS3bucketName();
        String key = UUID.randomUUID().toString();
        s3ItemDao.putItem(model, bucketName, key);
        return formatAddress(bucketName, key);
    }

    private ProductKeyBatchS3ObjectModel getProductKeyListFromS3(String address) {
        if (address == null) {
            return null;
        }
        String[] tempArr = unformatAddress(address);
        //ProductKeyBatchS3ObjectModel
        return s3ItemDao.getItem(
                tempArr[0],
                tempArr[1],
                ProductKeyBatchS3ObjectModel.class);
    }

    private ProductKeyBatch saveProductKeyBatch(ProductKeyBatch keyBatch) {
        ProductKeyBatchModel model = ProductKeyBatch.toModel(keyBatch);
        productkeyBatchDao.save(model);
        return ProductKeyBatch.fromModel(model);
    }

    private String formatAddress(String bucketName, String key) {
        return String.join("/", bucketName, key);
    }

    //return String[]{bucketName, key}
    private String[] unformatAddress(String address) {
        return address.split("/", 2);
    }
}
