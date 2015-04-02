package com.yunsoo.processor.controller;

import com.yunsoo.processor.controller.task.ProductKeyBatchMassage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
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
@RequestMapping("/productkeybatch")
public class ProductKeyBatchController {

    private static final String QUEUE_NAME = "dev-demo";

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public void sendToMessageQueue(@RequestBody ProductKeyBatchMassage message){
        queueMessagingTemplate.convertAndSend(QUEUE_NAME, message);
    }


    @MessageMapping(QUEUE_NAME)
    private void receiveMessage(ProductKeyBatchMassage message) {
        System.out.println("new message processed");
        System.out.println(message.getBatchId());
        System.out.println(message.getS3Url());
    }
}
