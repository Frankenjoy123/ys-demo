package com.yunsoo.api.key.service;

import com.yunsoo.api.client.KeyApiClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by:   Lijian
 * Created on:   2016-11-03
 * Descriptions:
 */
public class KeyBatchService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;

}
