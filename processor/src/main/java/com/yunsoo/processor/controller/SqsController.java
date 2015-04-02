package com.yunsoo.processor.controller;

import com.yunsoo.processor.message.ProductKeyBatchMassage;
import com.yunsoo.processor.handler.ProductKeyBatchHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
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

    private static final String HEADER_PAYLOADNAME = "PayloadName";

    private static final String QUEUE_NAME = "dev-productkeybatch";

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @RequestMapping(value = "/productkeybatch", method = RequestMethod.POST)
    public void sendToMessageQueue(@RequestBody ProductKeyBatchMassage message) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(HEADER_PAYLOADNAME, "productkeybatch");
        queueMessagingTemplate.convertAndSend(QUEUE_NAME, message, headers);
    }


    @MessageMapping(QUEUE_NAME)
    private void receiveMessage(ProductKeyBatchMassage message,
                                @Header(value = HEADER_PAYLOADNAME, required = false) String payloadName,
                                @Headers Map<String, Object> headers) {
        switch (payloadName) {
            case "productkeybatch":
                new ProductKeyBatchHandler();


                break;
            default:
                throw new RuntimeException("PayloadName invalid. MessageId:" + headers.get("MessageId"));
        }
        throw new RuntimeException("new exception");
    }
}
