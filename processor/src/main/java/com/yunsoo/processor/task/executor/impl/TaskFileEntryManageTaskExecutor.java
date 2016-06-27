package com.yunsoo.processor.task.executor.impl;

import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.message.ProductPackageMessage;
import com.yunsoo.common.data.object.TaskFileEntryObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.processor.domain.TaskFileDomain;
import com.yunsoo.processor.sqs.MessageSender;
import com.yunsoo.processor.task.Task;
import com.yunsoo.processor.task.annotation.Executor;
import com.yunsoo.processor.task.executor.TaskExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-06-22
 * Descriptions:
 */
@Executor("TaskFileEntryManage")
public class TaskFileEntryManageTaskExecutor implements TaskExecutor {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private TaskFileDomain taskFileDomain;

    @Autowired
    private MessageSender messageSender;


    @Override
    public void execute(Task task) {
        List<TaskFileEntryObject> taskFiles = getTaskFileEntries();
        if (taskFiles.size() > 0) {
            log.info(String.format("start processing %d task files", taskFiles.size()));
            for (TaskFileEntryObject f : taskFiles) {
                if (LookupCodes.TaskFileStatus.UPLOADED.equals(f.getStatusCode())) {
                    if (LookupCodes.TaskFileType.PACKAGE.equals(f.getTypeCode())) {
                        ProductPackageMessage message = new ProductPackageMessage();
                        message.setTaskFileId(f.getFileId());
                        messageSender.sendMessage(message);
                        taskFileDomain.updateTaskFileEntryStatus(f.getFileId(), LookupCodes.TaskFileStatus.PROCESSING);
                    }

                }
            }

        }
    }

    @Override
    public long getTimeout() {
        return 300 * 1000; //300s
    }

    private List<TaskFileEntryObject> getTaskFileEntries() {
        String typeCode = LookupCodes.TaskFileType.PACKAGE;
        List<String> statusCodes = Arrays.asList(LookupCodes.TaskFileStatus.UPLOADED, LookupCodes.TaskFileStatus.PROCESSING);
        Page<TaskFileEntryObject> page = taskFileDomain.getTaskFileEntryByFilter(null, null, null, typeCode, statusCodes, null, null, null, new PageRequest(0, 2));
        return page.getContent();
    }
}
