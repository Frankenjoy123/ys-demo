package com.yunsoo.processor.sqs.handler.impl;

import com.yunsoo.common.data.message.ProductPackageMessage;
import com.yunsoo.common.data.object.TaskFileEntryObject;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.processor.domain.TaskFileDomain;
import com.yunsoo.processor.sqs.handler.MessageHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by:   Lijian
 * Created on:   2016-06-23
 * Descriptions:
 */
@Component
public class ProductPackageHandler implements MessageHandler<ProductPackageMessage> {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private TaskFileDomain taskFileDomain;

    @Override
    public void process(ProductPackageMessage message) {
        String fileId = message.getTaskFileId();
        TaskFileEntryObject taskFileEntryObject = taskFileDomain.getTaskFileEntryById(fileId);
        if (taskFileEntryObject == null) {
            log.warn("taskFileEntry not found by id: " + fileId);
            return;
        }
        //check status of taskFileEntryObject
        //todo:

        YSFile taskFile = taskFileDomain.getTaskFile(taskFileEntryObject.getOrgId(), fileId);
        if (taskFile == null) {
            throw new RuntimeException("taskFile not found " + StringFormatter.formatMap("orgId", taskFileEntryObject.getOrgId(), "fileId", fileId));
        }

        //todo:


    }

}
