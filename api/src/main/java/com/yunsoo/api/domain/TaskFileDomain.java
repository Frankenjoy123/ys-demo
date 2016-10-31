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
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by:   Lijian
 * Created on:   2016-06-02
 * Descriptions:
 */
@Component
public class TaskFileDomain {

    private static final String[] FILE_TYPE_ARRAY = new String[]{LookupCodes.TaskFileType.PACKAGE, LookupCodes.TaskFileType.TRACE};
    private static final Pattern REGEXP_DATETIME_PREFIX = Pattern.compile("^\\d\\d\\d\\d\\-[01]\\d\\-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d\\.\\d\\d\\dZ.*");

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
            String name,
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
                .append("name", name)
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

    /**
     * file_type should be [package|trace]
     *
     * @param fileName  not required
     * @param ysFile    must contains file_type in header
     * @param appId     required
     * @param deviceId  not required
     * @param committed true: try override exists TaskFileEntry with the same name and status_code of uploading, or create new one, then change the status_code to uploaded or pending.
     *                  false: try override exists TaskFileEntry with the same name and status_code of uploading, or create new one, keep the status_code uploading.
     *                  null: always create new TaskFileEntry, then change the status_code to uploaded or pending.
     * @param ignored   true: change status_code to uploaded, job service will ignore the TaskFileEntry.
     *                  false|null: change status_code to pending, job service will process the TaskFileEntry.
     * @return new created file entry object
     */
    public TaskFileEntryObject saveYSFile(String fileName, YSFile ysFile, String appId, String deviceId, Boolean committed, Boolean ignored) {
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        String fileType = ysFile.getHeader("file_type");
        if (!validateFileType(fileType)) {
            throw new BadRequestException("file_type invalid. [file_type: " + fileType + "]");
        }

        TaskFileEntryObject obj = null;
        if (committed != null) {
            //try override file with the same name in status of uploading
            obj = getLastUploadingTaskFileEntry(orgId, appId, deviceId, fileName);
        }
        if (obj != null && LookupCodes.TaskFileStatus.UPLOADING.equals(obj.getStatusCode())) {
            obj.setProductBaseId(ysFile.getHeader("product_base_id"));
            obj.setPackageCount(tryParseInt(ysFile.getHeader("package_count")));
            obj.setPackageSize(tryParseInt(ysFile.getHeader("package_size")));
            obj.setProductCount(tryParseInt(ysFile.getHeader("product_count")));
            obj.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
            obj.setCreatedDateTime(DateTime.now());
            this.patchUpdateTaskFileEntry(obj);
        } else {
            obj = new TaskFileEntryObject();
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
        }

        //save file
        String path = String.format("organization/%s/task_file/%s", orgId, obj.getFileId());
        byte[] data = ysFile.toBytes();
        fileService.putFile(path, new ResourceInputStream(new ByteArrayInputStream(data), data.length, "text/plain"));

        //update status of the file entry
        if (committed == null || committed) {
            if (ignored != null && ignored) {
                this.updateTaskFileEntryStatus(obj.getFileId(), LookupCodes.TaskFileStatus.UPLOADED);
            } else {
                this.updateTaskFileEntryStatus(obj.getFileId(), LookupCodes.TaskFileStatus.PENDING);
            }
        }

        return this.getTaskFileEntryById(obj.getFileId());
    }

    public ResourceInputStream getFile(String orgId, String fileId) {
        String path = String.format("organization/%s/task_file/%s", orgId, fileId);
        return fileService.getFile(path);
    }

    public ResourceInputStream exportFile(String orgId, String fileId, String formatTypeCode) {
        String path = String.format("organization/%s/task_file/%s", orgId, fileId);
        ResourceInputStream fileStream = fileService.getFile(path);
        try {
            YSFile taskFile = YSFile.read(fileStream);

            if ("text_package_multiline".equals(formatTypeCode)) {
                if (!LookupCodes.TaskFileType.PACKAGE.equals(taskFile.getHeader("file_type"))) {
                    return null;
                }
                List<String> lines = Arrays.asList(new String(taskFile.getContent(), StandardCharsets.UTF_8).split("\r\n"));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                for (String line : lines) {
                    if (line.length() > 24 && REGEXP_DATETIME_PREFIX.matcher(line).matches()) {
                        line = line.substring(24, line.length()).trim();
                    }
                    String[] tempArray = line.split(":");
                    if (tempArray.length == 2 && tempArray[0].length() > 0) {
                        String[] tempSubArray = tempArray[1].split(",");
                        for (String c : tempSubArray) {
                            if (c.length() > 0) {
                                String subLine = String.format("%s,%s,%d\r\n", tempArray[0], c, tempSubArray.length);
                                outputStream.write(subLine.getBytes(StandardCharsets.UTF_8));
                            }
                        }
                    }
                }
                byte[] buffer = outputStream.toByteArray();
                return new ResourceInputStream(new ByteArrayInputStream(buffer), buffer.length, MediaType.TEXT_PLAIN_VALUE);
            } else if ("text_package_singleline".equals(formatTypeCode)) {
                if (!LookupCodes.TaskFileType.PACKAGE.equals(taskFile.getHeader("file_type"))) {
                    return null;
                }
                List<String> lines = Arrays.asList(new String(taskFile.getContent(), StandardCharsets.UTF_8).split("\r\n"));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                for (String line : lines) {
                    if (line.length() > 24 && REGEXP_DATETIME_PREFIX.matcher(line).matches()) {
                        line = line.substring(24, line.length()).trim();
                    }
                    outputStream.write(line.getBytes(StandardCharsets.UTF_8));
                    outputStream.write(new byte[]{13, 10});
                }
                byte[] buffer = outputStream.toByteArray();
                return new ResourceInputStream(new ByteArrayInputStream(buffer), buffer.length, MediaType.TEXT_PLAIN_VALUE);
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
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
        if (start == null)
            start = DateTime.now().minusMonths(1);
        if (end == null)
            end = DateTime.now();
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("device_id", deviceId)
                .append("type_code", typeCode)
                .append("status_code_in", statusCodeIn)
                .append("created_datetime_start", start)
                .append("created_datetime_end", end)
                .build();

        List<TaskFileEntryObject> dataList = dataApiClient.get("/taskFileEntry/sum/date" + query, new ParameterizedTypeReference<List<TaskFileEntryObject>>() {
        });

        Map<String, TaskFileEntryObject> mapData = new HashMap<>();
        dataList.forEach(item -> {
            mapData.put(item.getName(), item);
        });


        List<TaskFileEntryObject> resultList = new ArrayList<>();
        int totalLength = new Long((end.getMillis() - start.getMillis()) / (24 * 60 * 60 * 1000)).intValue() + 1;
        for (int i = 0; i < totalLength; i++) {
            String date = start.toDateTime(DateTimeZone.forID("+08:00")).toString("YYYY-MM-dd");
            TaskFileEntryObject object = new TaskFileEntryObject();
            object.setName(date);
            if (mapData.containsKey(date)) {
                object.setProductCount(mapData.get(date).getProductCount());
                object.setPackageCount(mapData.get(date).getPackageCount());
            }
            resultList.add(object);

            start = start.plusDays(1);
        }

        return resultList;
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

    //region private methods

    private TaskFileEntryObject createTaskFileEntry(TaskFileEntryObject obj) {
        obj.setFileId(null);
        obj.setStatusCode(LookupCodes.TaskFileStatus.UPLOADING);
        obj.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        obj.setCreatedDateTime(DateTime.now());
        return dataApiClient.post("taskFileEntry", obj, TaskFileEntryObject.class);
    }

    private TaskFileEntryObject getLastUploadingTaskFileEntry(String orgId, String appId, String deviceId, String name) {
        if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(deviceId) || StringUtils.isEmpty(name)) {
            return null;
        }
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append("app_id", appId)
                .append("device_id", deviceId)
                .append("name", name)
                .append("status_code_in", Collections.singletonList(LookupCodes.TaskFileStatus.UPLOADING))
                .build();
        List<TaskFileEntryObject> list = dataApiClient.getPaged("taskFileEntry" + query, new ParameterizedTypeReference<List<TaskFileEntryObject>>() {
        }).getContent();
        if (list.size() == 0) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    private void patchUpdateTaskFileEntry(TaskFileEntryObject obj) {
        if (obj != null && obj.getFileId() != null) {
            dataApiClient.patch("taskFileEntry/{id}", obj, obj.getFileId());
        }
    }

    private void updateTaskFileEntryStatus(String fileId, String statusCode) {
        try {
            dataApiClient.put("taskFileEntry/{id}/statusCode", statusCode, fileId);
        } catch (Exception e) {
            log.error("update statusCode failed for taskFileEntry " + StringFormatter.formatMap("fileId", fileId));
        }
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

    //endregion

}
