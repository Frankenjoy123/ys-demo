package com.yunsoo.processor.controller;

import com.yunsoo.common.data.message.ProductKeyBatchMassage;
import com.yunsoo.processor.common.LogicalQueueName;
import com.yunsoo.processor.common.PayloadName;
import com.yunsoo.processor.domain.SqsDomain;
import com.yunsoo.processor.handler.ProductKeyBatchHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/4/1
 * Descriptions:
 */
@RestController
@RequestMapping("/sqs")
public class SqsController {

    private static final String HEADER_PAYLOAD_NAME = "PayloadName";
    private static final Logger LOGGER = LoggerFactory.getLogger(SqsController.class);

    @Autowired
    private SqsDomain domain;

    @Autowired
    private ProductKeyBatchHandler productKeyBatchHandler;



    @RequestMapping(value = "/productkeybatch", method = RequestMethod.POST)
    public ProductKeyBatchMassage sendToMessageQueue(@RequestBody ProductKeyBatchMassage message) {
        domain.sendMessage(message);
        return message;
    }


    @MessageMapping(LogicalQueueName.PRODUCT_KEY_BATCH)
    private void receiveProductKeyBatchMassage(
            ProductKeyBatchMassage message,
            @Header(value = HEADER_PAYLOAD_NAME, required = false) String payloadName) {

        switch (payloadName) {
            case PayloadName.PRODUCT_KEY_BATCH:
                productKeyBatchHandler.execute(message);
                break;
            default:
                throw new RuntimeException("PayloadName invalid.");
        }
    }
}
