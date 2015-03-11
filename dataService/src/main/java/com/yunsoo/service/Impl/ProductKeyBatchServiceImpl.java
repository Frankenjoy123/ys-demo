package com.yunsoo.service.Impl;

import com.yunsoo.common.util.DateTimeUtils;
import com.yunsoo.dao.ProductKeyBatchDao;
import com.yunsoo.dao.ProductKeyDao;
import com.yunsoo.dao.S3ItemDao;
import com.yunsoo.dbmodel.ProductKeyBatchModel;
import com.yunsoo.dbmodel.ProductKeyBatchS3ObjectModel;
import com.yunsoo.dbmodel.ProductKeyModel;
import com.yunsoo.service.ProductKeyBatchService;
import com.yunsoo.service.contract.ProductKeyBatch;
import com.yunsoo.util.KeyGenerator;
import com.yunsoo.util.YunsooConfig;
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
    private ProductKeyDao productkeyDao;

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
        if (address != null) {
            String[] tempArr = unformatAddress(address);
            //ProductKeyBatchS3ObjectModel
            ProductKeyBatchS3ObjectModel s3ObjectModel = s3ItemDao.getItem(
                    tempArr[0],
                    tempArr[1],
                    ProductKeyBatchS3ObjectModel.class);
            return s3ObjectModel.getProductKeys();

        }
        return null;
    }

    @Override
    public ProductKeyBatch create(ProductKeyBatch keyBatch) {
        this.createProductKeys(keyBatch);

        keyBatch.setId(0); //create new item
        return save(keyBatch);
    }

    @Override
    public ProductKeyBatch createAsync(ProductKeyBatch keyBatch) {
        throw new NotImplementedException();
    }

    private void createProductKeys(ProductKeyBatch batch) {
        int quantity = batch.getQuantity();
        int[] keyTypeIds = batch.getProductKeyTypeIds() == null ? new int[0] : batch.getProductKeyTypeIds();

        Assert.isTrue(quantity > 0, "quantity must be greater than 0");
        Assert.isTrue(keyTypeIds.length > 0, "productKeyTypeIds must not be empty");

        List<ProductKeyModel> keyModelList = new ArrayList<>();
        List<List<String>> keyList = new ArrayList<>();

        if (keyTypeIds.length == 1) {
            //create one key each product
            for (int i = 0; i < quantity; i++) {
                String key = KeyGenerator.newKey();
                keyList.add(Arrays.asList(key));

                ProductKeyModel keyModel = new ProductKeyModel();
                keyModel.setProductKey(key);
                //keyModel.setStatusId(keyStatusId);
                keyModel.setProductKeyTypeId(keyTypeIds[0]);
                keyModelList.add(keyModel);
            }
        } else {
            //create multi keys each product
            for (int i = 0; i < quantity; i++) {
                List<String> tempKeys = new ArrayList<>(1);
                Set<String> keySet = new HashSet<>();
                String primaryKey = "";
                for (int j = 0; j < keyTypeIds.length; j++) {
                    String key = KeyGenerator.newKey();
                    tempKeys.add(key);
                    keySet.add(key);

                    ProductKeyModel keyModel = new ProductKeyModel();
                    keyModel.setProductKey(key);
                    //keyModel.setDisabled(keyStatusId);
                    keyModel.setProductKeyTypeId(keyTypeIds[j]);
                    if (j == 0) {
                        primaryKey = key;
                        keyModel.setProductKeySet(keySet);
                    } else {
                        keyModel.setPrimaryProductKey(primaryKey);
                    }
                    keyModelList.add(keyModel);
                }
                keyList.add(tempKeys);
            }
        }

        //save keyList to S3
        ProductKeyBatchS3ObjectModel s3ObjectModel = new ProductKeyBatchS3ObjectModel();
        s3ObjectModel.setId(batch.getId());
        s3ObjectModel.setQuantity(batch.getQuantity());
        s3ObjectModel.setCreatedDateTimeStr(DateTimeUtils.toString(batch.getCreatedDateTime()));
        s3ObjectModel.setProductKeyTypeIds(batch.getProductKeyTypeIds());
        s3ObjectModel.setProductKeys(keyList);
        String address = saveProductKeyBatchS3ObjectModel(s3ObjectModel);
        batch.setProductKeysAddress(address);
    }

    private ProductKeyBatch save(ProductKeyBatch keyBatch) {
        ProductKeyBatchModel model = ProductKeyBatch.toModel(keyBatch);
        productkeyBatchDao.save(model);
        return ProductKeyBatch.fromModel(model);
    }

    private String saveProductKeyBatchS3ObjectModel(ProductKeyBatchS3ObjectModel model) {
        String bucketName = YunsooConfig.getProductKeyBatchS3bucketName();
        String key = UUID.randomUUID().toString();
        s3ItemDao.putItem(model, bucketName, key);
        return formatAddress(bucketName, key);
    }


    //    @Override
//    public ProductKeyBatch create(ProductKeyBatch keyBatch) {
//        int quantity = keyBatch.getQuantity();
//        int keyStatusId = keyBatch.getStatusId();
//        int[] keyTypeIds = keyBatch.getProductKeyTypeIds();
//
//        if (quantity <= 0 || keyTypeIds == null || keyTypeIds.length == 0) {
//            return null;
//        }
//
//        Arrays.sort(keyTypeIds);
//        List<ProductKeyModel> keyModelList = new ArrayList<>();
//        List<List<String>> keyList = new ArrayList<>();
//
//        if (keyTypeIds.length == 1) {
//            //create one key each product
//            for (int i = 0; i < quantity; i++) {
//                String key = KeyGenerator.newKey();
//                List<String> tempKeys = new ArrayList<>(1);
//                tempKeys.add(key);
//                keyList.add(tempKeys);
//
//                ProductKeyModel keyModel = new ProductKeyModel();
//                keyModel.setProductKey(key);
//                //keyModel.setStatusId(keyStatusId);
//                keyModel.setProductKeyTypeId(keyTypeIds[0]);
//                keyModelList.add(keyModel);
//            }
//        } else {
//            //create multi keys each product
//            for (int i = 0; i < quantity; i++) {
//                List<String> tempKeys = new ArrayList<>(1);
//                Set<String> keySet = new HashSet<>();
//                String primaryKey = "";
//                for (int j = 0; j < keyTypeIds.length; j++) {
//                    String key = KeyGenerator.newKey();
//                    tempKeys.add(key);
//                    keySet.add(key);
//
//                    ProductKeyModel keyModel = new ProductKeyModel();
//                    keyModel.setProductKey(key);
//                    //keyModel.setDisabled(keyStatusId);
//                    keyModel.setProductKeyTypeId(keyTypeIds[j]);
//                    if (j == 0) {
//                        primaryKey = key;
//                        keyModel.setProductKeySet(keySet);
//                    } else {
//                        keyModel.setPrimaryProductKey(primaryKey);
//                    }
//                    keyModelList.add(keyModel);
//                }
//                keyList.add(tempKeys);
//            }
//        }
//
//        //save ProductKeyBatch
//        String batchId = UUID.randomUUID().toString();
//        ProductKeyBatchModel batchModel = new ProductKeyBatchModel();
//        batchModel.setId(batchId);
//        batchModel.setStatusId(keyBatch.getStatusId());
//        batchModel.setQuantity(quantity);
//        batchModel.setCreatedClientId(keyBatch.getCreatedClientId());
//        batchModel.setCreatedAccountId(keyBatch.getCreatedAccountId());
//        batchModel.setCreatedDateTime(keyBatch.getCreatedDateTime());
//        batchModel.setProductKeyTypeIds(keyBatch.getProductKeyTypeIds());
//
//        //save keyList to S3
//        ProductKeyBatchS3ObjectModel s3ObjectModel = new ProductKeyBatchS3ObjectModel(batchModel);
//        s3ObjectModel.setProductKeys(keyList);
//        try {
//            s3ItemDao.putItem(s3ObjectModel, YunsooConfig.getProductKeyBatchS3bucketName(), batchId);
//        } catch (Exception ex) {
//            return null;
//        }
//
//        String address = formatAddress(YunsooConfig.getProductKeyBatchS3bucketName(), batchId);
//        batchModel.setProductKeysAddress(address);
//        productkeyBatchDao.save(batchModel);
//
//
//        //save ProductKeys
//        keyModelList.forEach(m -> {
//            m.setBatchId(batchId);
//        });
//        productkeyDao.batchSave(keyModelList);
//
//        //result
//        ProductKeyBatch result = new ProductKeyBatch();
//        result.setId(batchId);
//        result.setQuantity(quantity);
//        result.setStatusId(batchModel.getStatusId());
//        result.setCreatedClientId(batchModel.getCreatedClientId());
//        result.setCreatedAccountId(batchModel.getCreatedAccountId());
//        result.setCreatedDateTime(batchModel.getCreatedDateTime());
//        result.setProductKeyTypeIds(keyTypeIds);
//        result.setProductKeys(keyList);
//
//        return result;
//    }
//
    private String formatAddress(String bucketName, String key) {
        return String.join("/", bucketName, key);
    }

    private String[] unformatAddress(String address) {
        return address.split("/", 2);
    }
}
