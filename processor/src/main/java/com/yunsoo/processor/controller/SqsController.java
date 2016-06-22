package com.yunsoo.processor.controller;

import com.yunsoo.common.data.message.ProductKeyBatchCreateMessage;
import com.yunsoo.processor.sqs.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MessageService messageService;


    @RequestMapping(value = "message/" + ProductKeyBatchCreateMessage.PAYLOAD_TYPE, method = RequestMethod.PUT)
    public void sendProductKeyBatchMassage(@RequestBody ProductKeyBatchCreateMessage message) {
        messageService.sendMessage(message);
    }

}
