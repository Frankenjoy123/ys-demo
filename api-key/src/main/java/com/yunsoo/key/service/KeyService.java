package com.yunsoo.key.service;

import com.yunsoo.key.dto.Key;
import com.yunsoo.key.dto.Product;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
public interface KeyService {

    Key get(String key);

    String formatExternalKey(String partitionId, String externalKey);

    void setDisabled(String key, Boolean disable);

    void batchSave(String keyBatchId,
                   List<String> keyTypeCodes,
                   List<List<String>> productKeys,
                   Product productTemplate);
}
