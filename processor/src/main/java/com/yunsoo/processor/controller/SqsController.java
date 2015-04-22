package com.yunsoo.processor.controller;

import com.yunsoo.processor.message.ProductKeyBatchMassage;
import com.yunsoo.processor.handler.ProductKeyBatchHandler;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String PRODUCTKEYBATCH_QUEUE_NAME = "dev-productkeybatch";

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    private ProductKeyBatchHandler productKeyBatchHandler;

    @RequestMapping(value = "/productkeybatch", method = RequestMethod.POST)
    public void sendToMessageQueue(@RequestBody ProductKeyBatchMassage message) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(HEADER_PAYLOAD_NAME, "productkeybatch");
        queueMessagingTemplate.convertAndSend(PRODUCTKEYBATCH_QUEUE_NAME, message, headers);
    }


    @MessageMapping(PRODUCTKEYBATCH_QUEUE_NAME)
    private void receiveMessage(ProductKeyBatchMassage message,
                                @Header(value = HEADER_PAYLOAD_NAME, required = false) String payloadName) {
        switch (payloadName) {
            case "productkeybatch":
                productKeyBatchHandler.execute(message);
                break;
            default:
                throw new RuntimeException("PayloadName invalid.");
        }
    }
}
