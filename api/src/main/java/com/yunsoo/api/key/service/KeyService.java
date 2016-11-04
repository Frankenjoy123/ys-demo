package com.yunsoo.api.key.service;

import com.yunsoo.api.client.KeyApiClient;
import com.yunsoo.api.key.dto.Key;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-11-03
 * Descriptions:
 */
@Service
public class KeyService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;


    public Key getKey(String key) {
        if (!StringUtils.hasText(key)) {
            return null;
        }
        try {
            return keyApiClient.get("key/{key}", Key.class, key);
        } catch (NotFoundException e) {
            log.warn("key not found, key: " + key, e);
            return null;
        }
    }

    public Key getExternalKey(String partitionId, String externalKey) {
        if (!StringUtils.hasText(partitionId) || !StringUtils.hasText(externalKey)) {
            return null;
        }
        try {
            return keyApiClient.get("key/external/{partitionId}/{externalKey}", Key.class, partitionId, externalKey);
        } catch (NotFoundException e) {
            log.warn("key not found " + StringFormatter.formatMap("partitionId", partitionId, "externalKey", externalKey));
            return null;
        }
    }

    public void enableKey(String key) {
        try {
            keyApiClient.post("key/{key}/enable", null, null, key);
        } catch (NotFoundException e) {
            log.warn("key not found, key: " + key, e);
            throw new NotFoundException("key not found, key: " + key);
        }
    }

    public void disableKey(String key) {
        try {
            keyApiClient.post("key/{key}/disable", null, null, key);
        } catch (NotFoundException e) {
            log.warn("key not found, key: " + key, e);
            throw new NotFoundException("key not found, key: " + key);
        }
    }

}
