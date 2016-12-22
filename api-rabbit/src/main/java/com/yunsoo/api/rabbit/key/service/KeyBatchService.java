package com.yunsoo.api.rabbit.key.service;

import com.yunsoo.api.rabbit.client.KeyApiClient;
import com.yunsoo.api.rabbit.key.dto.KeyBatch;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-12-07
 * Descriptions:
 */
@Service
public class KeyBatchService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;

    public KeyBatch getKeyBatchById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        try {
            return keyApiClient.get("keyBatch/{id}", KeyBatch.class, id);
        } catch (NotFoundException e) {
            log.warn("keyBatch not found, id: " + id);
            return null;
        }
    }

}
