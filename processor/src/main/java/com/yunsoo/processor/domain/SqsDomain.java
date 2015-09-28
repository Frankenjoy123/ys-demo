package com.yunsoo.processor.domain;

import com.yunsoo.common.data.message.ProductKeyBatchMassage;
import com.yunsoo.processor.common.LogicalQueueName;
import com.yunsoo.processor.common.PayloadName;
import com.yunsoo.processor.config.CustomQueueMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yan on 8/27/2015.
 */
@Component
public class SqsDomain {

    private static final String HEADER_PAYLOAD_NAME = "PayloadName";
    private static final Logger LOGGER = LoggerFactory.getLogger(SqsDomain.class);

    @Autowired
   // private QueueMessagingTemplate queueMessagingTemplate;
    private CustomQueueMessagingTemplate queueMessagingTemplate;
    @Autowired
    ResourceIdResolver resourceIdResolver;

    public void sendMessage(ProductKeyBatchMassage message){
        sendMessage(message, 0l);
    }

    public void sendMessage(ProductKeyBatchMassage message, Long delaySecondes){
        Map<String, Object> headers = new HashMap<>();
        headers.put(HEADER_PAYLOAD_NAME, PayloadName.PRODUCT_KEY_BATCH);
        queueMessagingTemplate.convertAndSend(LogicalQueueName.PRODUCT_KEY_BATCH, message, headers, delaySecondes);
        LOGGER.info("new payload [name: {}, message: {}] pushed to queue [{}]",
                PayloadName.PRODUCT_KEY_BATCH,
                message.toString(),
                resourceIdResolver.resolveToPhysicalResourceId(LogicalQueueName.PRODUCT_KEY_BATCH));

    }



}
