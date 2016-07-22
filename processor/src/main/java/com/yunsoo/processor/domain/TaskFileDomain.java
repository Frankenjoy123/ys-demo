package com.yunsoo.processor.domain;

import com.yunsoo.common.data.object.TaskFileEntryObject;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import com.yunsoo.processor.client.DataApiClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-06-02
 * Descriptions:
 */
@Component
public class TaskFileDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private DataApiClient dataApiClient;

    @Autowired
    private FileDomain fileDomain;


    public TaskFileEntryObject getTaskFileEntryById(String fileId) {
        if (fileId == null || fileId.length() == 0) {
            return null;
        }
        try {
            return dataApiClient.get("taskFileEntry/{id}", TaskFileEntryObject.class, fileId);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public Page<TaskFileEntryObject> getTaskFileEntryByFilter(
            String orgId,
            String appId,
            String deviceId,
            String typeCode,
            List<String> statusCodeIn,
            String createdAccountId,
            DateTime createdDateTimeStart,
            DateTime createdDateTimeEnd,
            Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("app_id", appId)
                .append("device_id", deviceId)
                .append("type_code", typeCode)
                .append("status_code_in", statusCodeIn)
                .append("created_account_id", createdAccountId)
                .append("created_datetime_start", createdDateTimeStart)
                .append("created_datetime_end", createdDateTimeEnd)
                .append(pageable)
                .build();
        return dataApiClient.getPaged("taskFileEntry" + query, new ParameterizedTypeReference<List<TaskFileEntryObject>>() {
        });
    }

    public void updateTaskFileEntryStatus(String fileId, String statusCode) {
        try {
            dataApiClient.put("taskFileEntry/{id}/statusCode", statusCode, fileId);
        } catch (Exception e) {
            log.error("update statusCode failed for taskFileEntry " + StringFormatter.formatMap("fileId", fileId));
        }
    }

    public YSFile getTaskFile(String orgId, String fileId) {
        if (orgId == null || orgId.length() == 0 || fileId == null || fileId.length() == 0) {
            return null;
        }
        String path = String.format("organization/%s/task_file/%s", orgId, fileId);
        return fileDomain.getYSFile(path);
    }

}