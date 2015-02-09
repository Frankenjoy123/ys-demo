package com.yunsoo.service.Impl;

import com.yunsoo.dao.ProductKeyBatchDao;
import com.yunsoo.dbmodel.ProductKeyBatchModel;
import com.yunsoo.service.ProductKeyBatchService;
import com.yunsoo.service.contract.ProductKeyBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
@Service("productKeyBatchService")
public class ProductKeyBatchServiceImpl implements ProductKeyBatchService {

    @Autowired
    private ProductKeyBatchDao productkeyBatchDao;


    @Override
    public ProductKeyBatch getById(String id) {
        ProductKeyBatch keyBatch =null;
        ProductKeyBatchModel keyBatchModel = productkeyBatchDao.getById(id);
        if(keyBatchModel != null) {
            keyBatch = new ProductKeyBatch();
            keyBatch.setId(keyBatchModel.getId());
            keyBatch.setQuantity(keyBatchModel.getQuantity());
            keyBatch.setStatusId(keyBatchModel.getStatusId());
            keyBatch.setCreatedClientId(keyBatchModel.getCreatedClientId());
            keyBatch.setCreatedAccountId(keyBatchModel.getCreatedAccountId());
            keyBatch.setCreatedDateTime(keyBatchModel.getCreatedDateTime());
            keyBatch.setProductKeyTypeIds(keyBatchModel.getProductKeyTypeIds());

            String keySetAddress = keyBatchModel.getProductKeySetAddress();

        }
        return keyBatch;
    }
}
