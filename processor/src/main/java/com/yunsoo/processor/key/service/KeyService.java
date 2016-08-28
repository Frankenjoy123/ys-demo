package com.yunsoo.processor.key.service;

import com.yunsoo.processor.client.KeyApiClient;
import com.yunsoo.processor.key.dto.BatchSaveKeyRequest;
import com.yunsoo.processor.key.dto.KeyBatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-28
 * Descriptions:
 */
@Service
public class KeyService {

    Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;

    public void batchSaveKeys(KeyBatch keyBatch, List<List<String>> keys, String productStatusCode) {
        BatchSaveKeyRequest requestObject = new BatchSaveKeyRequest();
        requestObject.setKeyBatchId(keyBatch.getId());
        requestObject.setKeyTypeCodes(keyBatch.getKeyTypeCodes());
        requestObject.setKeys(keys);
        requestObject.setProductBaseId(keyBatch.getProductBaseId());
        requestObject.setProductStatusCode(productStatusCode);
        requestObject.setCreatedDateTime(keyBatch.getCreatedDateTime());

        keyApiClient.post("key/batchSave", requestObject, null);
    }
}
