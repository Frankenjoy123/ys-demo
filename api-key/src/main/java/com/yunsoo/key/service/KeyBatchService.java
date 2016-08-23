package com.yunsoo.key.service;

import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.key.dto.KeyBatch;
import com.yunsoo.key.dto.KeyBatchCreationRequest;
import com.yunsoo.key.dto.Keys;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface KeyBatchService {

    KeyBatch getById(String batchId);

    Keys getKeysById(String batchId);


    KeyBatch create(KeyBatchCreationRequest request);

    void patchUpdate(KeyBatch batch);


    ResourceInputStream getKeyBatchDetails(String batchId);

    void saveKeyBatchDetails(String batchId, ResourceInputStream details);

}
