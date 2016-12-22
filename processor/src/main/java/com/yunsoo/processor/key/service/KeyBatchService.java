package com.yunsoo.processor.key.service;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.processor.Constants;
import com.yunsoo.processor.client.KeyApiClient;
import com.yunsoo.processor.key.dto.KeyBatch;
import com.yunsoo.processor.key.dto.Keys;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-08-28
 * Descriptions:
 */
@Service
public class KeyBatchService {

    Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;


    public KeyBatch getById(String batchId) {
        if (StringUtils.isEmpty(batchId)) {
            return null;
        }
        try {
            return keyApiClient.get("keyBatch/{id}", KeyBatch.class, batchId);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Keys getKeysByBatchId(String batchId) {
        if (StringUtils.isEmpty(batchId)) {
            return null;
        }
        try {
            return keyApiClient.get("keyBatch/{id}/keys", Keys.class, batchId);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public void setKeyBatchStatusToAvailable(String keyBatchId) {
        KeyBatch keyBatch = new KeyBatch();
        keyBatch.setStatusCode(Constants.KeyBatchStatus.AVAILABLE);
        keyApiClient.patch("keyBatch/{id}", keyBatch, keyBatchId);
    }
}
