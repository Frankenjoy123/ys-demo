package com.yunsoo.api.rabbit.key.service;

import com.yunsoo.api.rabbit.client.KeyApiClient;
import com.yunsoo.api.rabbit.key.dto.Key;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by:   Lijian
 * Created on:   2016-08-23
 * Descriptions:
 */
@Service
public class KeyService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;


    public String translateExternalKey(String partitionId, String externalKey) {
        try {
            Key key = keyApiClient.get("key/external/{partitionId}/{externalKey}", Key.class, partitionId, externalKey);
            if (key.isDisabled()) {
                return null;
            }
            if (key.isPrimary()) {
                for (String k : key.getKeySet()) {
                    if (!String.format("%s/%s", partitionId, externalKey).equals(k)) {
                        return k;
                    }
                }
                return null;
            } else {
                return key.getPrimaryKey();
            }
        } catch (NotFoundException e) {
            log.warn("key not found " + StringFormatter.formatMap("partitionId", partitionId, "externalKey", externalKey));
            return null;
        }
    }

}
