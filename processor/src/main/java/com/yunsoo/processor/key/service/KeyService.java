package com.yunsoo.processor.key.service;

import com.yunsoo.processor.client.KeyApiClient;
import com.yunsoo.processor.key.dto.BatchSaveKeyRequest;
import com.yunsoo.processor.key.dto.KeyBatch;
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

    @Autowired
    private KeyApiClient keyApiClient;

    public void batchSaveKeys(KeyBatch keyBatch, String productStatusCode, String serialNoPattern, List<List<String>> keys) {
        BatchSaveKeyRequest request = new BatchSaveKeyRequest();
        request.setKeyBatchId(keyBatch.getId());
        request.setKeyTypeCodes(keyBatch.getKeyTypeCodes());
        request.setProductBaseId(keyBatch.getProductBaseId());
        request.setCreatedDateTime(keyBatch.getCreatedDateTime());
        request.setProductStatusCode(productStatusCode);
        request.setSerialNoPattern(serialNoPattern);
        request.setKeys(keys);

        keyApiClient.post("key/batchSave", request, null);
    }
}
