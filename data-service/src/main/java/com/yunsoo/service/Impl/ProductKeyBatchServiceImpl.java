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
import com.yunsoo.config.AmazonSetting;
import com.yunsoo.util.KeyGenerator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    @Autowired
    private AmazonSetting amazonSetting;


    @Override
    public ProductKeyBatch getById(int batchId) {
        ProductKeyBatchModel model = productkeyBatchDao.getById(batchId);
        return model == null ? null : ProductKeyBatch.fromModel(model);
    }

    @Override
    public List<List<String>> getProductKeysByBatchId(int batchId) {
        ProductKeyBatch keyBatch = this.getById(batchId);
        if (keyBatch == null) {
            return null;
        }
        return getProductKeysByAddress(keyBatch.getProductKeysAddress());
    }

    @Override
    public List<List<String>> getProductKeysByAddress(String address) {
        ProductKeyBatchS3ObjectModel model = getProductKeyListFromS3(address);
        return model == null ? null : model.getProductKeys();
    }

    @Override
    public ProductKeyBatch create(ProductKeyBatch batch) {
        return create(batch, null);
    }

    @Override
    public ProductKeyBatch create(ProductKeyBatch batch, Product productTemplate) {
        int quantity = batch.getQuantity();
        List<Integer> keyTypeIds = batch.getProductKeyTypeIds();

        Assert.isTrue(quantity > 0, "quantity must be greater than 0");
        Assert.isTrue(keyTypeIds.size() > 0, "productKeyTypeIds must not be empty");

        //generate productKeys
        List<List<String>> keyList = generateProductKeys(batch);

        //save batch to get the id
        batch.setId(0); //set to 0 for creating new item
        if (batch.getCreatedDateTime() == null) batch.setCreatedDateTime(DateTime.now());
        ProductKeyBatch newBatch = saveProductKeyBatch(batch);

        //save keyList to S3
        String address = saveProductKeyListToS3(newBatch, keyList);

        //update batch with address
        newBatch.setProductKeysAddress(address);
        newBatch = updateProductKeyBatch(newBatch);

        //generate ProductModel List
        List<ProductModel> productModel = generateProductModelList(newBatch, productTemplate, keyList);

        //save productModel
        productDao.batchSave(productModel);

        return newBatch;
    }

    @Override
    public ProductKeyBatch createAsync(ProductKeyBatch batch) {
        return createAsync(batch, null);
    }

    @Override
    public ProductKeyBatch createAsync(ProductKeyBatch batch, Product productTemplate) {

        return null;
    }

    //private methods

    private List<List<String>> generateProductKeys(ProductKeyBatch batch) {
        int quantity = batch.getQuantity();
        List<Integer> keyTypeIds = batch.getProductKeyTypeIds();

        List<List<String>> keyList = new ArrayList<>(quantity);

        for (int i = 0, len = keyTypeIds.size(); i < quantity; i++) {
            List<String> tempKeys = new ArrayList<>(len);
            for (int j = 0; j < len; j++) {
                tempKeys.add(KeyGenerator.newKey());
            }
            keyList.add(tempKeys);
        }

        return keyList;
    }

    private List<ProductModel> generateProductModelList(ProductKeyBatch batch, Product productTemplate, List<List<String>> keyList) {
        int quantity = batch.getQuantity();
        List<Integer> keyTypeIds = batch.getProductKeyTypeIds();

        Assert.isTrue(quantity > 0 && quantity == keyList.size(), "keyList invalid");

        List<ProductModel> productModelList = new ArrayList<>(quantity * keyTypeIds.size());
        if (keyTypeIds.size() == 1) {
            keyList.stream().forEach(keys -> {
                if (keys != null && keys.size() > 0) {
                    ProductModel productModel = generateProductModel(productTemplate);
                    productModel.setProductKey(keys.get(0));
                    productModel.setProductKeyTypeId(keyTypeIds.get(0));
                    productModel.setProductKeyBatchId(batch.getId());
                    productModel.setCreatedDateTime(batch.getCreatedDateTime());
                    productModelList.add(productModel);
                }
            });
        } else { //multi keys for each product
            keyList.stream().forEach(keys -> {
                if (keys != null && keys.size() >= keyTypeIds.size()) {
                    Set<String> keySet = new HashSet<>();
                    String primaryKey = keys.get(0);
                    for (int j = 0; j < keyTypeIds.size(); j++) {
                        String key = keys.get(j);
                        keySet.add(key);
                        ProductModel productModel = generateProductModel(productTemplate);
                        productModel.setProductKey(key);
                        productModel.setProductKeyTypeId(keyTypeIds.get(j));
                        productModel.setProductKeyBatchId(batch.getId());
                        productModel.setCreatedDateTime(batch.getCreatedDateTime());
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
        }
        return model;
    }

    private String saveProductKeyListToS3(ProductKeyBatch batch, List<List<String>> keyList) {
        ProductKeyBatchS3ObjectModel model = new ProductKeyBatchS3ObjectModel();
        model.setId(batch.getId());
        model.setQuantity(batch.getQuantity());
        model.setCreatedDateTime(DateTimeUtils.toString(batch.getCreatedDateTime()));
        model.setProductKeyTypeIds(batch.getProductKeyTypeIds());
        model.setProductKeys(keyList);
        String bucketName = amazonSetting.getS3_basebucket(); // YunsooConfig.getBaseBucket();
        String id = Integer.toString(batch.getId()) + "_" + UUID.randomUUID().toString();
        String key = String.join("/", amazonSetting.getS3_product_key_batch_path(), id);
        s3ItemDao.putItem(bucketName, key, model);
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
        if (keyBatch.getCreatedDateTime() == null) {
            keyBatch.setCreatedDateTime(DateTime.now());
        }
        ProductKeyBatchModel model = ProductKeyBatch.toModel(keyBatch);
        productkeyBatchDao.save(model);
        return ProductKeyBatch.fromModel(model);
    }

    private ProductKeyBatch updateProductKeyBatch(ProductKeyBatch keyBatch) {
        ProductKeyBatchModel model = ProductKeyBatch.toModel(keyBatch);
        productkeyBatchDao.update(model);
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
