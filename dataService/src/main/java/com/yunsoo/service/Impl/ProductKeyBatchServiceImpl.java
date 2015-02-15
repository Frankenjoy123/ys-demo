package com.yunsoo.service.Impl;

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
    public ProductKeyBatch getById(String id) {
        ProductKeyBatch keyBatch = null;
        ProductKeyBatchModel keyBatchModel = productkeyBatchDao.getById(id);
        if (keyBatchModel != null) {
            keyBatch = new ProductKeyBatch();
            keyBatch.setId(keyBatchModel.getId());
            keyBatch.setQuantity(keyBatchModel.getQuantity());
            keyBatch.setStatusId(keyBatchModel.getStatusId());
            keyBatch.setCreatedClientId(keyBatchModel.getCreatedClientId());
            keyBatch.setCreatedAccountId(keyBatchModel.getCreatedAccountId());
            keyBatch.setCreatedDateTime(keyBatchModel.getCreatedDateTime());
            keyBatch.setProductKeyTypeIds(keyBatchModel.getProductKeyTypeIds());
            keyBatch.setProductKeysAddress(keyBatchModel.getProductKeysAddress());
        }
        return keyBatch;
    }

    @Override
    public ProductKeyBatch getByIdWithProductKeys(String batchId) {
        ProductKeyBatch keyBatch = this.getById(batchId);

        //todo: get productKeys from S3
        String address = keyBatch.getProductKeysAddress();

        if (address != null) {
            String[] tempArr = unformatAddress(address);
            //ProductKeyBatchS3ObjectModel
            ProductKeyBatchS3ObjectModel s3ObjectModel = s3ItemDao.getItem(
                    tempArr[0],
                    tempArr[1],
                    ProductKeyBatchS3ObjectModel.class);
            if (s3ObjectModel != null) {
                keyBatch.setProductKeys(s3ObjectModel.getProductKeys());
            }

        }

        return keyBatch;
    }

    @Override
    public ProductKeyBatch create(ProductKeyBatch keyBatch) {
        int quantity = keyBatch.getQuantity();
        int keyStatusId = keyBatch.getStatusId();
        int[] keyTypeIds = keyBatch.getProductKeyTypeIds();

        if (quantity <= 0 || keyTypeIds == null || keyTypeIds.length == 0) {
            return null;
        }

        Arrays.sort(keyTypeIds);
        List<ProductKeyModel> keyModelList = new ArrayList<>();
        List<List<String>> keyList = new ArrayList<>();

        if (keyTypeIds.length == 1) {
            //create one key each product
            for (int i = 0; i < quantity; i++) {
                String key = KeyGenerator.newKey();
                List<String> tempKeys = new ArrayList<>(1);
                tempKeys.add(key);
                keyList.add(tempKeys);

                ProductKeyModel keyModel = new ProductKeyModel();
                keyModel.setProductKey(key);
                keyModel.setStatusId(keyStatusId);
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
                    keyModel.setStatusId(keyStatusId);
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

        //save ProductKeyBatch
        String batchId = UUID.randomUUID().toString();
        ProductKeyBatchModel batchModel = new ProductKeyBatchModel();
        batchModel.setId(batchId);
        batchModel.setStatusId(keyBatch.getStatusId());
        batchModel.setQuantity(quantity);
        batchModel.setCreatedClientId(keyBatch.getCreatedClientId());
        batchModel.setCreatedAccountId(keyBatch.getCreatedAccountId());
        batchModel.setCreatedDateTime(keyBatch.getCreatedDateTime());
        batchModel.setProductKeyTypeIds(keyBatch.getProductKeyTypeIds());

        //save keyList to S3
        ProductKeyBatchS3ObjectModel s3ObjectModel = new ProductKeyBatchS3ObjectModel(batchModel);
        s3ObjectModel.setProductKeys(keyList);
        try {
            s3ItemDao.putItem(s3ObjectModel, YunsooConfig.getProductKeyBatchS3bucketName(), batchId);
        } catch (Exception ex) {
            return null;
        }

        String address = formatAddress(YunsooConfig.getProductKeyBatchS3bucketName(), batchId);
        batchModel.setProductKeysAddress(address);
        productkeyBatchDao.save(batchModel);


        //save ProductKeys
        keyModelList.forEach(m -> {
            m.setBatchId(batchId);
        });
        productkeyDao.batchSave(keyModelList);

        //result
        ProductKeyBatch result = new ProductKeyBatch();
        result.setId(batchId);
        result.setQuantity(quantity);
        result.setStatusId(batchModel.getStatusId());
        result.setCreatedClientId(batchModel.getCreatedClientId());
        result.setCreatedAccountId(batchModel.getCreatedAccountId());
        result.setCreatedDateTime(batchModel.getCreatedDateTime());
        result.setProductKeyTypeIds(keyTypeIds);
        result.setProductKeys(keyList);

        return result;
    }

    private String formatAddress(String bucketName, String key) {
        return String.join("/", bucketName, key);
    }

    private String[] unformatAddress(String address) {
        return address.split("/", 2);
    }
}
