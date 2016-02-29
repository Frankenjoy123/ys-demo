package com.yunsoo.processor.controller;

import com.yunsoo.common.data.message.ProductKeyBatchMassage;
import com.yunsoo.processor.common.LogicalQueueName;
import com.yunsoo.processor.common.PayloadName;
import com.yunsoo.processor.domain.SqsDomain;
import com.yunsoo.processor.handler.ProductKeyBatchHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2015/4/1
 * Descriptions:
 */
@RestController
@RequestMapping("/sqs")
public class SqsController {

    private static final String HEADER_PAYLOAD_NAME = "PayloadName";
    private Log log = LogFactory.getLog(this.getClass());

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
