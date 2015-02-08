package com.yunsoo.service.Impl;

import com.yunsoo.dbmodel.ProductKeyBatchModel;
import com.yunsoo.dbmodel.ProductKeyModel;
import com.yunsoo.service.ProductKeyService;
import com.yunsoo.dao.*;

import java.util.*;

import com.yunsoo.service.contract.ProductKeyBatchCreateRequest;
import com.yunsoo.service.contract.ProductKeyBatchCreateResponse;
import com.yunsoo.util.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
@Service
public class ProductKeyServiceImpl implements ProductKeyService {

    @Autowired
    private ProductKeyDao productkeyDao;

    @Autowired
    private ProductKeyBatchDao productkeyBatchDao;


    @Override
    public ProductKeyBatchCreateResponse batchCreate(ProductKeyBatchCreateRequest request) {
        ProductKeyBatchCreateResponse response = new ProductKeyBatchCreateResponse();
        int quantity = request.getQuantity();
        int[] keyTypeIds = request.getProductKeyTypeIds();

        if (quantity <= 0 || keyTypeIds == null || keyTypeIds.length == 0) {
            response.setQuantity(0);
            return response;
        }

        List<ProductKeyModel> keyModels = new ArrayList<>();

        if (keyTypeIds.length == 1) {
            for (int i = 0; i < quantity; i++) {
                String key = KeyGenerator.newKey();
                ProductKeyModel keyModel = new ProductKeyModel();
                keyModel.setStatusId(request.getStatusId());
                keyModel.setProductKeyTypeId(keyTypeIds[0]);
                keyModel.setProductKey(key);
                keyModels.add(keyModel);
            }
        } else {
            Arrays.sort(keyTypeIds);
            for (int i = 0; i < quantity; i++) {
                Set<String> keySet = new HashSet<>();
                String primaryKey = "";
                for (int j = 0; j < keyTypeIds.length; j++) {
                    String key = KeyGenerator.newKey();
                    ProductKeyModel keyModel = new ProductKeyModel();
                    keyModel.setStatusId(request.getStatusId());
                    keyModel.setProductKeyTypeId(keyTypeIds[j]);
                    keyModel.setProductKey(key);
                    keySet.add(key);
                    if (j == 0) {
                        primaryKey = key;
                        keyModel.setProductKeySet(keySet);
                    } else {
                        keyModel.setPrimaryProductKey(primaryKey);
                    }
                    keyModels.add(keyModel);
                }
            }
        }

        ProductKeyBatchModel batchModel = new ProductKeyBatchModel();
        batchModel.setStatusId(0);
        batchModel.setQuantity(quantity);
        batchModel.setCreatedClientId(request.getCreatedClientId());
        batchModel.setCreatedAccountId(request.getCreatedAccountId());
        batchModel.setCreatedDateTime(request.getCreatedDateTime());
        batchModel.setProductKeyTypeIds(request.getProductKeyTypeIds());
        batchModel.setProductKeySetAddress("http://localhost"); //todo
        productkeyBatchDao.save(batchModel);
        String batchId = batchModel.getId();
        keyModels.forEach(m -> {
            m.setBatchId(batchId);
        });
        productkeyDao.batchSave(keyModels);

        response.setQuantity(quantity);
        response.setProductKeyTypeIds(keyTypeIds);
        response.setBatchId(batchId);
        response.setStatusId(request.getStatusId());
        response.setCreatedClientId(request.getCreatedClientId());
        response.setCreatedAccountId(request.getCreatedAccountId());
        response.setCreatedDateTime(request.getCreatedDateTime());
        return response;
    }
}
