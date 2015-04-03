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
import com.yunsoo.service.contract.ProductKeys;
import com.yunsoo.util.KeyGenerator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

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
    public ProductKeyBatch getById(Long id) {
        ProductKeyBatchModel model = productkeyBatchDao.getById(id);
        return model == null ? null : ProductKeyBatch.fromModel(model);
    }

    @Override
    public List<ProductKeyBatch> getByOrganizationIdPaged(Integer organizationId, int pageIndex, int pageSize) {
        Map<String, Object> eqFilter = new HashMap<>();
        if (organizationId != null) {
            eqFilter.put("organizationId", organizationId);
        }
        return productkeyBatchDao.getByFilterPaged(eqFilter, pageIndex, pageSize).stream()
                .map(ProductKeyBatch::fromModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductKeyBatch> getByFilterPaged(Integer organizationId, Long productBaseId, int pageIndex, int pageSize) {
        Map<String, Object> eqFilter = new HashMap<>();
        if (organizationId != null) {
            eqFilter.put("organizationId", organizationId);
        }
        eqFilter.put("productBaseId", productBaseId); //productBaseId can be null
        return productkeyBatchDao.getByFilterPaged(eqFilter, pageIndex, pageSize).stream()
                .map(ProductKeyBatch::fromModel)
                .collect(Collectors.toList());
    }

    @Override
    public ProductKeys getProductKeysByBatchId(Long batchId) {
        ProductKeyBatch keyBatch = this.getById(batchId);
        if (keyBatch == null) {
            return null;
        }
        return getProductKeysByAddress(keyBatch.getProductKeysAddress());
    }

    @Override
    public ProductKeys getProductKeysByAddress(String address) {
        ProductKeyBatchS3ObjectModel model = getProductKeyListFromS3(address);
        if (model == null) {
            return null;
        }
        ProductKeys productKeys = new ProductKeys();
        productKeys.setBatchId(model.getId());
        productKeys.setQuantity(model.getQuantity());
        productKeys.setCreatedDateTime(DateTimeUtils.parse(model.getCreatedDateTime()));
        productKeys.setProductKeyTypeIds(model.getProductKeyTypeIds());
        productKeys.setProductKeys(model.getProductKeys());
        return productKeys;
    }

    @Override
    public ProductKeyBatch create(ProductKeyBatch batch) {
        int quantity = batch.getQuantity();
        List<Integer> keyTypeIds = batch.getProductKeyTypeIds();

        Assert.isTrue(quantity > 0, "quantity must be greater than 0");
        Assert.isTrue(keyTypeIds.size() > 0, "productKeyTypeIds must not be empty");

        //generate productKeys
        List<List<String>> keyList = generateProductKeys(batch);

        //save batch to get the id
        batch.setId(0L); //set to 0 for creating new item
        if (batch.getCreatedDateTime() == null) batch.setCreatedDateTime(DateTime.now());
        ProductKeyBatch newBatch = saveProductKeyBatch(batch);

        //save keyList to S3
        String address = saveProductKeyListToS3(newBatch, keyList);

        //update batch with address
        newBatch.setProductKeysAddress(address);
        newBatch = updateProductKeyBatch(newBatch);

        return newBatch;
    }

    @Override
    public void batchSaveProductKey(ProductKeyBatch batch, List<List<String>> keyList, Product productTemplate) {
        //generate ProductModel List
        List<ProductModel> productModels = generateProductModelList(batch, keyList, productTemplate);

        //save productModel
        productDao.batchSave(productModels);
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

    private List<ProductModel> generateProductModelList(ProductKeyBatch batch, List<List<String>> keyList, Product productTemplate) {
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
        String id = Long.toString(batch.getId()) + "_" + UUID.randomUUID().toString();
        String key = String.join("/", amazonSetting.getS3_product_key_batch_path(), id);
        s3ItemDao.putItem(bucketName, key, model);
        return formatAddress(bucketName, key);
    }

    private ProductKeyBatchS3ObjectModel getProductKeyListFromS3(String address) {
        if (address == null) {
            return null;
        }
        String[] tempArr = splitAddress(address);
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
    private String[] splitAddress(String address) {
        return address.split("/", 2);
    }
}
