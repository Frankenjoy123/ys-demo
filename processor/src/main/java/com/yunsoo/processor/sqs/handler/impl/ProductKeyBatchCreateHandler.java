package com.yunsoo.processor.sqs.handler.impl;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.processor.domain.LogDomain;
import com.yunsoo.processor.domain.ProductKeyDomain;
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
    private ProductKeyDomain productKeyDomain;

    @Autowired
    private MessageSender messageSender;

    @Autowired
    private LogDomain logDomain;

    @Override
    public void process(KeyBatchCreationMessage message) {
        String productKeyBatchId = message.getKeyBatchId();
        String productStatusCode = !StringUtils.isEmpty(message.getProductStatusCode())
                ? message.getProductStatusCode()
                : LookupCodes.ProductStatus.CREATED;

        ProductKeyBatchObject productKeyBatchObject = productKeyDomain.getProductKeyBatchById(productKeyBatchId);
        if (productKeyBatchObject == null) {
            log.error("product key batch not found " + StringFormatter.formatMap("id", productKeyBatchId));
            throw new RuntimeException("product key batch not found");
        }
        if (!LookupCodes.ProductKeyBatchStatus.CREATING.equals(productKeyBatchObject.getStatusCode())) {
            log.error("productkeybatch status is not valid " + StringFormatter.formatMap(
                    "message", message, "productKeyBatchStatusCode", productKeyBatchObject.getStatusCode()));
            throw new RuntimeException("product key batch status is not valid");
        }

        List<List<String>> productKeys = productKeyDomain.getProductKeys(productKeyBatchObject);
        if (productKeys == null) {
            log.error("product keys not found " + StringFormatter.formatMap(
                    "orgId", productKeyBatchObject.getOrgId(),
                    "productKeyBatchId", productKeyBatchObject.getId()));
            throw new RuntimeException("product keys not found");
        }

        int quantity = productKeys.size();
        int continueOffset = message.getContinueOffset() != null ? message.getContinueOffset() : 0;

        log.info("started processing productKeyBatch " + StringFormatter.formatMap("message", message, "remainQuantity", quantity - continueOffset));
        DateTime startDateTime = DateTime.now();
        DateTime timeOutDateTime = startDateTime.plusSeconds(TIMEOUT_SECONDS);
        DateTime batchStartDateTime;

        for (int i = continueOffset; i < quantity; i += BATCH_LIMIT) {
            int toIndex = quantity < i + BATCH_LIMIT ? quantity : i + BATCH_LIMIT;
            List<List<String>> subList = productKeys.subList(i, toIndex);

            batchStartDateTime = DateTime.now();
            productKeyDomain.batchCreateProductKeys(productKeyBatchObject, subList, productStatusCode);
            DateTime batchEndDateTime = DateTime.now();

            log.info("batch saved productKeys " + StringFormatter.formatMap(
                    "count", toIndex - i,
                    "seconds", (batchEndDateTime.getMillis() - batchStartDateTime.getMillis()) / 1000.0));

            if (toIndex < quantity && timeOutDateTime.getMillis() < batchEndDateTime.getMillis()) {
                //timeout
                log.info("timeout processing productKeyBatch " + StringFormatter.formatMap(
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
        productKeyDomain.updateProductKeyBatchStatus(productKeyBatchId, LookupCodes.ProductKeyBatchStatus.AVAILABLE);

        DateTime endDateTime = DateTime.now();
        logDomain.logInfo(KeyBatchCreationMessage.PAYLOAD_TYPE,
                "finished " + StringFormatter.formatMap("totalSeconds", (endDateTime.getMillis() - productKeyBatchObject.getCreatedDateTime().getMillis()) / 1000.0),
                productKeyBatchId,
                "product_key_batch_id");
        log.info("finished processing productKeyBatch " + StringFormatter.formatMap(
                "message", message,
                "seconds", (endDateTime.getMillis() - startDateTime.getMillis()) / 1000.0,
                "totalSeconds", (endDateTime.getMillis() - productKeyBatchObject.getCreatedDateTime().getMillis()) / 1000.0));
    }

}
