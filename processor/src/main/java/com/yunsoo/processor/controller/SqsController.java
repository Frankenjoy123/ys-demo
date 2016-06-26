package com.yunsoo.processor.controller;

import com.yunsoo.common.data.message.ProductKeyBatchCreateMessage;
import com.yunsoo.common.data.message.ProductPackageMessage;
import com.yunsoo.processor.sqs.MessageSender;
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


    @RequestMapping(value = "message/" + ProductKeyBatchCreateMessage.PAYLOAD_TYPE, method = RequestMethod.PUT)
    public void sendProductKeyBatchCreateMassage(@RequestBody ProductKeyBatchCreateMessage message) {
        messageSender.sendMessage(message);
    }

    @RequestMapping(value = "message/" + ProductPackageMessage.PAYLOAD_TYPE, method = RequestMethod.PUT)
    public void sendProductPackageMassage(@RequestBody ProductPackageMessage message) {
        messageSender.sendMessage(message);
    }

}
