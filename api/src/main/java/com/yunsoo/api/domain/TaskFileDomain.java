package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataApiClient;
import com.yunsoo.api.file.service.FileService;
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
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-06-02
 * Descriptions:
 */
@Component
public class TaskFileDomain {

    private static final String[] FILE_TYPE_ARRAY = new String[]{LookupCodes.TaskFileType.PACKAGE, LookupCodes.TaskFileType.TRACE};

    @Autowired
    private DataApiClient dataApiClient;

    @Autowired
    private FileService fileService;

    private Log log = LogFactory.getLog(this.getClass());


    public TaskFileEntryObject getTaskFileEntryById(String fileId) {
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
            DateTime createdDateTimeGE,
            DateTime createdDateTimeLE,
            Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("app_id", appId)
                .append("device_id", deviceId)
                .append("type_code", typeCode)
                .append("status_code_in", statusCodeIn)
                .append("created_account_id", createdAccountId)
                .append("created_datetime_ge", createdDateTimeGE)
                .append("created_datetime_le", createdDateTimeLE)
                .append(pageable)
                .build();
        return dataApiClient.getPaged("taskFileEntry" + query, new ParameterizedTypeReference<List<TaskFileEntryObject>>() {
        });
    }

    public void updateStatusToUploaded(String fileId) {
        try {
            dataApiClient.put("taskFileEntry/{id}/statusCode", LookupCodes.TaskFileStatus.UPLOADED, fileId);
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
        obj.setProductBaseId(ysFile.getHeader("product_base_id"));
        obj.setPackageCount(tryParseInt(ysFile.getHeader("package_count")));
        obj.setPackageSize(tryParseInt(ysFile.getHeader("package_size")));
        obj.setProductCount(tryParseInt(ysFile.getHeader("product_count")));
        //create file entry
        obj = this.createTaskFileEntry(obj);

        String path = String.format("organization/%s/task_file/%s", orgId, obj.getFileId());
        byte[] data = ysFile.toBytes();
        //save file
        fileService.putFile(path, new ResourceInputStream(new ByteArrayInputStream(data), data.length, "text/plain"));

        //update status of the file entry
        this.updateStatusToUploaded(obj.getFileId());

        return this.getTaskFileEntryById(obj.getFileId());
    }

    public ResourceInputStream getFile(String orgId, String fileId){
        String path = String.format("organization/%s/task_file/%s", orgId, fileId);
        return fileService.getFile(path);
    }


    public TaskFileEntryObject getTotal(String orgId, String appId, String deviceId, String typeCode, DateTime start, DateTime end, List<String> statusCodeIn) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("app_id", appId)
                .append("device_id", deviceId)
                .append("type_code", typeCode)
                .append("status_code_in", statusCodeIn)
                .append("created_datetime_start", start)
                .append("created_datetime_end", end)
                .build();

        return dataApiClient.get("/taskFileEntry/sum" + query, TaskFileEntryObject.class);
    }

    public int countByDevice(String orgId, List<String> deviceId, String typeCode, List<String> statusCodeIn) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("device_ids", deviceId)
                .append("type_code", typeCode)
                .append("status_code_in", statusCodeIn)
                .build();

        return dataApiClient.get("/taskFileEntry/count" + query, Integer.class);
    }

    public List<TaskFileEntryObject> getTotalByDate(String deviceId, String typeCode, DateTime start, DateTime end, List<String> statusCodeIn) {
        if(start == null && end == null)
            start = DateTime.now().minusDays(90);
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("device_id", deviceId)
                .append("type_code", typeCode)
                .append("status_code_in", statusCodeIn)
                .append("created_datetime_start", start)
                .append("created_datetime_end", end)
                .build();

        List<TaskFileEntryObject> resultList = dataApiClient.get("/taskFileEntry/sum/date" + query, new ParameterizedTypeReference<List<TaskFileEntryObject>>() {
        });
        if(resultList.size() > 0) {
            DateTime lastDate = DateTime.parse(resultList.get(resultList.size() - 1).getName()+ "T00:00");
            while (DateTime.now().getDayOfYear() > lastDate.getDayOfYear()){
                lastDate = lastDate.plusDays(1);
                TaskFileEntryObject object = new TaskFileEntryObject();
                object.setName(lastDate.toString("YYYY-MM-dd"));
                object.setProductCount(0);
                object.setPackageCount(0);
                resultList.add(object);
            }
        }

        return  resultList;
    }

    public List<TaskFileEntryObject> getTotalByDevice(List<String> deviceId, String typeCode, DateTime start, DateTime end, List<String> statusCodeIn) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("device_id", deviceId)
                .append("type_code", typeCode)
                .append("status_code_in", statusCodeIn)
                .append("created_datetime_start", start)
                .append("created_datetime_end", end)
                .build();

        return dataApiClient.get("/taskFileEntry/sum/device" + query, new ParameterizedTypeReference<List<TaskFileEntryObject>>() {
        });
    }

    private TaskFileEntryObject createTaskFileEntry(TaskFileEntryObject obj) {
        obj.setFileId(null);
        obj.setStatusCode(LookupCodes.TaskFileStatus.UPLOADING);
        obj.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        obj.setCreatedDateTime(DateTime.now());
        return dataApiClient.post("taskFileEntry", obj, TaskFileEntryObject.class);
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

    private Long tryParseLong(String intValue) {
        if (StringUtils.isEmpty(intValue)) {
            return null;
        }
        try {
            return Long.parseLong(intValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer tryParseInt(String intValue) {
        if (StringUtils.isEmpty(intValue)) {
            return null;
        }
        try {
            return Integer.parseInt(intValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
