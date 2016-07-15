package com.yunsoo.processor.sqs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.common.data.message.ProductKeyBatchCreateMessage;
import com.yunsoo.common.data.message.ProductPackageMessage;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.processor.domain.LogDomain;
import com.yunsoo.processor.sqs.handler.impl.ProductKeyBatchCreateHandler;
import com.yunsoo.processor.sqs.handler.impl.ProductPackageHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.core.env.ResourceIdResolver;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by:   Lijian
 * Created on:   2016-06-23
 * Descriptions:
 */
@Service
public class MessageReceiver {

    private static final String HEADER_PAYLOAD_TYPE = "PayloadType";
    private static final String QUEUE_NAME_PROCESSOR = "processor";

    private static ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LogDomain logDomain;


    @Autowired
    ResourceIdResolver resourceIdResolver;

    @Autowired
    private ProductKeyBatchCreateHandler productKeyBatchCreateHandler;

    @Autowired
    private ProductPackageHandler productPackageHandler;


    @MessageMapping(QUEUE_NAME_PROCESSOR)
    public void receiveAndProcessMessage(
            String message,
            @Header(value = HEADER_PAYLOAD_TYPE, required = false) String payloadType) throws IOException {

        log.info("receive message from queue " + StringFormatter.formatMap(
                "payloadType", payloadType,
                "message", message,
                "queueName", resourceIdResolver.resolveToPhysicalResourceId(QUEUE_NAME_PROCESSOR)));

        switch (payloadType) {
            case ProductKeyBatchCreateMessage.PAYLOAD_TYPE:
                ProductKeyBatchCreateMessage pkbMsg = null;
                try {
                    pkbMsg = parseMessage(message, ProductKeyBatchCreateMessage.class);
                    productKeyBatchCreateHandler.process(pkbMsg);
                } catch (Exception e) {
                    log.error("product_key_batch_create failed with exception " + StringFormatter.formatMap("message", pkbMsg), e);
                    logDomain.logError(ProductKeyBatchCreateMessage.PAYLOAD_TYPE, e.getMessage(), pkbMsg != null ? pkbMsg.getProductKeyBatchId() : null, "product_key_batch_id");
                    throw e;
                }
                break;
            case ProductPackageMessage.PAYLOAD_TYPE:
                ProductPackageMessage ppMsg = null;
                try {
                    ppMsg = parseMessage(message, ProductPackageMessage.class);
                    productPackageHandler.process(ppMsg);
                } catch (Exception e) {
                    log.error("product_package failed with exception " + StringFormatter.formatMap("message", ppMsg), e);
                    logDomain.logError(ProductPackageMessage.PAYLOAD_TYPE, e.getMessage(), ppMsg != null ? ppMsg.getTaskFileId() : null, "task_file_id");
                    throw e;
                }
                break;
            default:
                throw new RuntimeException("PayloadType invalid");
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
