package com.yunsoo.key.processor.service;

import com.yunsoo.key.client.ProcessorClient;
import com.yunsoo.key.processor.dto.KeyBatchCreationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by:   Lijian
 * Created on:   2016-08-23
 * Descriptions:
 */
@Service
public class ProcessorService {

    @Autowired
    private ProcessorClient processorClient;


    public void putKeyBatchCreateMessageToQueue(String keyBatchId, String productStatusCode) {
        KeyBatchCreationMessage message = new KeyBatchCreationMessage();
        message.setKeyBatchId(keyBatchId);
        message.setProductStatusCode(productStatusCode);
        processorClient.put("sqs/message/{payLoad}", message, KeyBatchCreationMessage.PAYLOAD_TYPE);
    }

}
