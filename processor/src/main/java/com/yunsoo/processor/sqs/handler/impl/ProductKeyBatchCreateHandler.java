package com.yunsoo.processor.sqs.handler.impl;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.util.SerialNoGenerator;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.processor.domain.LogDomain;
import com.yunsoo.processor.key.dto.KeyBatch;
import com.yunsoo.processor.key.dto.Keys;
import com.yunsoo.processor.key.service.KeyBatchService;
import com.yunsoo.processor.key.service.KeyService;
import com.yunsoo.processor.sqs.MessageSender;
import com.yunsoo.processor.sqs.handler.MessageHandler;
import com.yunsoo.processor.sqs.message.KeyBatchCreationMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-04-26
 * Descriptions:
 */
@Component
public class ProductKeyBatchCreateHandler implements MessageHandler<KeyBatchCreationMessage> {

    private static final int BATCH_LIMIT = 1000;
    private static final int TIMEOUT_SECONDS = 600;
    private static final long DELAY_SECONDS = 60;

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private KeyBatchService keyBatchService;

    @Autowired
    private KeyService keyService;

    @Autowired
    private LogDomain logDomain;

    @Override
    public void process(KeyBatchCreationMessage message) {
        String keyBatchId = message.getKeyBatchId();

        KeyBatch keyBatch = keyBatchService.getById(keyBatchId);
        if (keyBatch == null) {
            log.error("keyBatch not found by id: " + keyBatchId);
            throw new RuntimeException("key batch not found");
        }
        if (!LookupCodes.ProductKeyBatchStatus.CREATING.equals(keyBatch.getStatusCode())) {
            log.error("keyBatch status not valid " + StringFormatter.formatMap("message", message, "keyBatchStatusCode", keyBatch.getStatusCode()));
            throw new RuntimeException("keyBatch status not valid");
        }

        String productStatusCode = !StringUtils.isEmpty(message.getProductStatusCode()) ? message.getProductStatusCode()
                : !StringUtils.isEmpty(keyBatch.getProductStatusCode()) ? keyBatch.getProductStatusCode()
                : LookupCodes.ProductStatus.ACTIVATED;
        Keys keys = keyBatchService.getKeysByBatchId(keyBatchId);
        if (keys == null || keys.getKeys() == null) {
            log.error("keys not found by keyBatchId: " + keyBatchId);
            throw new RuntimeException("keys not found");
        }

        List<List<String>> keyList = keys.getKeys();
        int quantity = keyList.size();
        int continueOffset = message.getContinueOffset() != null ? message.getContinueOffset() : 0;
        SerialNoGenerator serialNoGenerator = !StringUtils.isEmpty(keys.getSerialNoPattern())
                ? new SerialNoGenerator(keys.getSerialNoPattern())
                : null;

        log.info("started processing keyBatch " + StringFormatter.formatMap("message", message, "remainQuantity", quantity - continueOffset));
        DateTime startDateTime = DateTime.now();
        DateTime timeOutDateTime = startDateTime.plusSeconds(TIMEOUT_SECONDS);
        DateTime batchStartDateTime;

        for (int i = continueOffset; i < quantity; i += BATCH_LIMIT) {
            int toIndex = quantity < i + BATCH_LIMIT ? quantity : i + BATCH_LIMIT;
            String serialNoPattern = serialNoGenerator != null ? serialNoGenerator.getSubSerialNoPattern(i, toIndex - i) : null;
            List<List<String>> subList = keyList.subList(i, toIndex);

            batchStartDateTime = DateTime.now();
            keyService.batchSaveKeys(keyBatch, productStatusCode, serialNoPattern, subList);
            DateTime batchEndDateTime = DateTime.now();

            log.info("batch saved keys " + StringFormatter.formatMap(
                    "count", toIndex - i,
                    "seconds", (batchEndDateTime.getMillis() - batchStartDateTime.getMillis()) / 1000.0));

            if (toIndex < quantity && timeOutDateTime.getMillis() < batchEndDateTime.getMillis()) {
                //timeout
                log.info("timeout processing keyBatch " + StringFormatter.formatMap(
                        "message", message,
                        "processedCount", toIndex - continueOffset,
                        "seconds", (batchEndDateTime.getMillis() - startDateTime.getMillis()) / 1000.0));

                message.setContinueOffset(toIndex);
                messageSender.sendMessage(message, DELAY_SECONDS); //send another new message with delay seconds
                log.info("continuous message sent " + StringFormatter.formatMap("message", message));
                return;
            }
        }

        //finished successfully
        keyBatchService.setKeyBatchStatusToAvailable(keyBatchId);

        DateTime endDateTime = DateTime.now();
        logDomain.logInfo(KeyBatchCreationMessage.PAYLOAD_TYPE,
                "finished " + StringFormatter.formatMap("totalSeconds", (endDateTime.getMillis() - keyBatch.getCreatedDateTime().getMillis()) / 1000.0),
                keyBatchId,
                "key_batch_id");
        log.info("finished processing keyBatch " + StringFormatter.formatMap(
                "message", message,
                "seconds", (endDateTime.getMillis() - startDateTime.getMillis()) / 1000.0,
                "totalSeconds", (endDateTime.getMillis() - keyBatch.getCreatedDateTime().getMillis()) / 1000.0));
    }

}
