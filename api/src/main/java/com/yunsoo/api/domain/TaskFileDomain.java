package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.TaskFileEntryObject;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-06-02
 * Descriptions:
 */
@Component
public class TaskFileDomain {

    private static final String[] FILE_TYPE_ARRAY = new String[]{"package", "logistics"};

    @Autowired
    private DataAPIClient dataAPIClient;

    @Autowired
    private FileDomain fileDomain;

    private Log log = LogFactory.getLog(this.getClass());


    public TaskFileEntryObject getTaskFileEntryById(String fileId) {
        try {
            return dataAPIClient.get("taskFileEntry/{id}", TaskFileEntryObject.class, fileId);
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
        return dataAPIClient.getPaged("taskFileEntry" + query, new ParameterizedTypeReference<List<TaskFileEntryObject>>() {
        });
    }

    public TaskFileEntryObject createTaskFileEntry(TaskFileEntryObject obj) {
        obj.setFileId(null);
        obj.setStatusCode(LookupCodes.TaskFileStatus.CREATED);
        obj.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        obj.setCreatedDateTime(DateTime.now());
        return dataAPIClient.post("taskFileEntry", obj, TaskFileEntryObject.class);
    }

    public void updateStatusToUploaded(String fileId) {
        try {
            dataAPIClient.put("taskFileEntry/{id}/statusCode", LookupCodes.TaskFileStatus.UPLOADED, fileId);
        } catch (Exception e) {
            log.error("update statusCode failed for taskFileEntry " + StringFormatter.formatMap("fileId", fileId));
        }
    }

    /**
     * file_type should be [package|logistics]
     *
     * @param fileName not required
     * @param ysFile   must contains file_type in header
     * @param appId    required
     * @param deviceId not required
     * @return new created file entry object
     */
    public TaskFileEntryObject saveYSFile(String fileName, YSFile ysFile, String appId, String deviceId) {
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        String fileType = ysFile.getHeader("file_type");
        if (!validateFileType(fileType)) {
            throw new BadRequestException("file_type invalid. [file_type: " + fileType + "]");
        }

        TaskFileEntryObject obj = new TaskFileEntryObject();
        obj.setOrgId(orgId);
        obj.setAppId(appId);
        obj.setDeviceId(deviceId);
        obj.setName(fileName);
        obj.setTypeCode(fileType);
        //create file entry
        obj = this.createTaskFileEntry(obj);

        String path = String.format("organization/%s/task_file/%s", orgId, obj.getFileId());
        byte[] data = ysFile.toBytes();
        //save file
        fileDomain.putFile(path, new ResourceInputStream(new ByteArrayInputStream(data), data.length, "text/plain"));

        //update status of the file entry
        this.updateStatusToUploaded(obj.getFileId());

        return this.getTaskFileEntryById(obj.getFileId());
    }

    private boolean validateFileType(String fileType) {
        if (fileType == null || fileType.length() == 0) return false;
        for (String t : FILE_TYPE_ARRAY) {
            if (t.equals(fileType)) {
                return true;
            }
        }
        return false;
    }

}
