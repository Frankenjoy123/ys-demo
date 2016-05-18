package com.yunsoo.processor.sqs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.common.data.message.ProductKeyBatchCreateMessage;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.processor.domain.LogDomain;
import com.yunsoo.processor.sqs.handler.impl.ProductKeyBatchCreateHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-04-26
 * Descriptions:
 */
@Service
public class MessageService {

    private static final String HEADER_PAYLOAD_TYPE = "PayloadType";
    private static final String QUEUE_NAME_PROCESSOR = "processor";

    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LogDomain logDomain;

    @Autowired
    private CustomQueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    ResourceIdResolver resourceIdResolver;

    @Autowired
    private ProductKeyBatchCreateHandler productKeyBatchCreateHandler;


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

    @MessageMapping(QUEUE_NAME_PROCESSOR)
    private void receiveAndProcessMessage(
            String message,
            @Header(value = HEADER_PAYLOAD_TYPE, required = false) String payloadType) throws IOException {

        log.info("receive message from queue " + StringFormatter.formatMap(
                "payloadType", payloadType,
                "message", message,
                "queueName", resourceIdResolver.resolveToPhysicalResourceId(QUEUE_NAME_PROCESSOR)));

        switch (payloadType) {
            case ProductKeyBatchCreateMessage.PAYLOAD_NAME:
                ProductKeyBatchCreateMessage messageObj = null;
                try {
                    messageObj = parseMessage(message, ProductKeyBatchCreateMessage.class);
                    productKeyBatchCreateHandler.process(messageObj);
                } catch (Exception e) {
                    logDomain.logError("product_key_batch_create", e.getMessage(), messageObj != null ? messageObj.getProductKeyBatchId() : null, "product_key_batch_id");
                    throw e;
                }
                break;
            default:
                throw new RuntimeException("PayloadType invalid");
        }
    }

    private String getPayloadTypeByMessageType(Class<?> messageType) {
        if (messageType.isAssignableFrom(ProductKeyBatchCreateMessage.class)) {
            return ProductKeyBatchCreateMessage.PAYLOAD_NAME;
        } else {
            throw new RuntimeException("message type not recognized");
        }
    }

    private <T> T parseMessage(String messageStr, Class<T> type) {
        try {
            return objectMapper.readValue(messageStr, type);
        } catch (IOException e) {
            log.error("message format issue " + StringFormatter.formatMap("message", messageStr));
            throw new RuntimeException("message format issue");
        }
    }

}
