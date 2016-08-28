package com.yunsoo.processor.controller;

import com.yunsoo.common.data.message.ProductPackageMessage;
import com.yunsoo.processor.sqs.MessageSender;
import com.yunsoo.processor.sqs.message.KeyBatchCreationMessage;
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
    private MessageSender messageSender;


    @RequestMapping(value = "message/" + KeyBatchCreationMessage.PAYLOAD_TYPE, method = RequestMethod.PUT)
    public void sendProductKeyBatchCreateMassage(@RequestBody KeyBatchCreationMessage message) {
        messageSender.sendMessage(message, 5); //send with delay 5 seconds
    }

    @RequestMapping(value = "message/" + ProductPackageMessage.PAYLOAD_TYPE, method = RequestMethod.PUT)
    public void sendProductPackageMassage(@RequestBody ProductPackageMessage message) {
        messageSender.sendMessage(message);
    }

}
