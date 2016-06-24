package com.yunsoo.processor.sqs;

import com.yunsoo.common.data.message.ProductKeyBatchCreateMessage;
import com.yunsoo.common.data.message.ProductPackageMessage;
import com.yunsoo.common.util.StringFormatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-06-23
 * Descriptions:
 */
@Service
public class MessageSender {

    private static final String HEADER_PAYLOAD_TYPE = "PayloadType";
    private static final String QUEUE_NAME_PROCESSOR = "processor";

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private CustomQueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    ResourceIdResolver resourceIdResolver;


    public <T> void sendMessage(T message) {
        String payloadType = getPayloadTypeByMessageType(message.getClass());
        Map<String, Object> headers = new HashMap<>();
        headers.put(HEADER_PAYLOAD_TYPE, payloadType);
        queueMessagingTemplate.convertAndSend(QUEUE_NAME_PROCESSOR, message, headers);

        log.info("message pushed to queue " + StringFormatter.formatMap(
                "payloadType", payloadType,
                "message", message,
                "queueName", resourceIdResolver.resolveToPhysicalResourceId(QUEUE_NAME_PROCESSOR)));
    }

    public <T> void sendMessage(T message, long delaySeconds) {
        String payloadType = getPayloadTypeByMessageType(message.getClass());
        Map<String, Object> headers = new HashMap<>();
        headers.put(HEADER_PAYLOAD_TYPE, payloadType);
        queueMessagingTemplate.convertAndSend(QUEUE_NAME_PROCESSOR, message, headers, delaySeconds);

        log.info("message pushed to queue " + StringFormatter.formatMap(
                "payloadType", payloadType,
                "message", message,
                "delaySeconds", delaySeconds,
                "queueName", resourceIdResolver.resolveToPhysicalResourceId(QUEUE_NAME_PROCESSOR)));
    }

    private String getPayloadTypeByMessageType(Class<?> messageType) {
        if (messageType.isAssignableFrom(ProductKeyBatchCreateMessage.class)) {
            return ProductKeyBatchCreateMessage.PAYLOAD_TYPE;
        } else if (messageType.isAssignableFrom(ProductPackageMessage.class)) {
            return ProductPackageMessage.PAYLOAD_TYPE;
        } else {
            throw new RuntimeException("message type not recognized");
        }
    }
}
