package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.TaskFileDomain;
import com.yunsoo.api.dto.TaskFileEntry;
import com.yunsoo.api.security.AuthDetails;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.object.TaskFileEntryObject;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.FileNameUtils;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-06-02
 * Descriptions:
 */
@RestController
@RequestMapping("taskFile")
public class TaskFileController {

    private static final int FILENAME_LENGTH_LIMIT = 50;

    @Autowired
    private TaskFileDomain taskFileDomain;

    private Log log = LogFactory.getLog(this.getClass());

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TaskFileEntry> getTaskFileByFilter(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "app_id", required = false) String appId,
            @RequestParam(value = "device_id", required = false) String deviceId,
            @RequestParam(value = "type_code", required = false) String typeCode,
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
            @RequestParam(value = "created_account_id", required = false) String createdAccountId,
            @RequestParam(value = "created_datetime_ge", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeGE,
            @RequestParam(value = "created_datetime_le", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeLE,
            Pageable pageable,
            HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId);
        Page<TaskFileEntryObject> page = taskFileDomain.getTaskFileEntryByFilter(orgId, appId, deviceId, typeCode, statusCodeIn, createdAccountId, createdDateTimeGE, createdDateTimeLE, pageable);

        return PageUtils.response(response, page.map(TaskFileEntry::new), pageable != null);
    }

    @RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> download(@PathVariable("id")String id) throws UnsupportedEncodingException {
        TaskFileEntryObject entry = taskFileDomain.getTaskFileEntryById(id);
        if (entry == null) {
            throw new NotFoundException("taskFile not found");
        }
        ResourceInputStream resourceInputStream = taskFileDomain.getFile(entry.getOrgId(), entry.getFileId());
        if (resourceInputStream == null) {
            throw new NotFoundException("attachment not found");
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        builder.header("Content-Disposition", "attachment;filename=" + URLEncoder.encode(entry.getName(), "UTF-8"));
        return builder.body(new InputStreamResource(resourceInputStream));
    }


    @RequestMapping(value = "form", method = RequestMethod.POST)
    public TaskFileEntry uploadInForm(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException();
        }
        AuthDetails authDetails = AuthUtils.getAuthDetails();
        String appId = authDetails.getAppId();
        String deviceId = authDetails.getDeviceId();

        log.info("received file in form " + StringFormatter.formatMap("size", file.getSize(), "appId", appId, "deviceId", deviceId));
        YSFile ysFile = readYSFile(file.getInputStream());

        TaskFileEntryObject obj = taskFileDomain.saveYSFile(FileNameUtils.shorten(file.getOriginalFilename(), FILENAME_LENGTH_LIMIT), ysFile, appId, deviceId);
        return new TaskFileEntry(obj);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public TaskFileEntry uploadInBody(@RequestParam(value = "file_name", required = false) String fileName,
                                      HttpServletRequest request) throws IOException {
        AuthDetails authDetails = AuthUtils.getAuthDetails();
        String appId = authDetails.getAppId();
        String deviceId = authDetails.getDeviceId();

        log.info("received file in request body "
                + StringFormatter.formatMap("size", request.getContentLengthLong(), "appId", appId, "deviceId", deviceId));
        YSFile ysFile = readYSFile(request.getInputStream());

        TaskFileEntryObject obj = taskFileDomain.saveYSFile(FileNameUtils.shorten(fileName, FILENAME_LENGTH_LIMIT), ysFile, appId, deviceId);
        return new TaskFileEntry(obj);
    }


    @RequestMapping(value = "sum", method = RequestMethod.GET)
    public TaskFileEntry getTotalTaskFiles(@RequestParam(value = "device_id", required = false) String deviceId,
                                                @RequestParam(value = "app_id", required = false) String appId,
                                                @RequestParam(value = "type_code") String typeCode,
                                                @RequestParam(value = "created_datetime_start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime start,
                                                @RequestParam(value = "created_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime end) {

        String orgId = AuthUtils.fixOrgId(null);
        List<String> statusCodeIn = Arrays.asList(LookupCodes.TaskFileStatus.FINISHED);
        TaskFileEntry result = new TaskFileEntry(taskFileDomain.getTotal(orgId,appId, deviceId,typeCode, start, end,statusCodeIn));
        result.setDeviceId(deviceId);
        result.setAppId(appId);
        result.setOrgId(orgId);
        return result;
    }

    @RequestMapping(value = "count", method = RequestMethod.GET)
    public int countTotalTaskFiles(@RequestParam(value = "device_ids", required = false) List<String> deviceId,
                                           @RequestParam(value = "type_code") String typeCode){

        String orgId = AuthUtils.fixOrgId(null);
        List<String> statusCodeIn = Arrays.asList(LookupCodes.TaskFileStatus.FINISHED);

        return taskFileDomain.countByDevice(orgId, deviceId, typeCode, statusCodeIn);

    }

    @RequestMapping(value = "sum/date", method = RequestMethod.GET)
    public List<TaskFileEntry> getTotalTaskFilesByDate(@RequestParam(value = "device_id") String deviceId,
                                                             @RequestParam(value = "type_code") String typeCode,
                                                             @RequestParam(value = "created_datetime_start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime start,
                                                             @RequestParam(value = "created_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime end) {

        List<String> statusCodeIn = Arrays.asList(LookupCodes.TaskFileStatus.FINISHED);
        List<TaskFileEntryObject> returnObj  = taskFileDomain.getTotalByDate(deviceId, typeCode, start, end, statusCodeIn);
        return returnObj.stream().map(TaskFileEntry::new).collect(Collectors.toList());

    }

    @RequestMapping(value = "sum/device", method = RequestMethod.GET)
    public List<TaskFileEntry> getTotalTaskFilesByDevice(@RequestParam(value = "device_ids") List<String> deviceId,
                                                       @RequestParam(value = "type_code") String typeCode,
                                                       @RequestParam(value = "created_datetime_start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime start,
                                                       @RequestParam(value = "created_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime end) {

        List<String> statusCodeIn = Arrays.asList(LookupCodes.TaskFileStatus.FINISHED);
        List<TaskFileEntryObject> returnObj  = taskFileDomain.getTotalByDevice(deviceId, typeCode, start, end, statusCodeIn);
        return returnObj.stream().map(TaskFileEntry::new).collect(Collectors.toList());

    }


    private YSFile readYSFile(InputStream inputStream) {
        YSFile ysFile;
        try {
            ysFile = YSFile.read(inputStream);
        } catch (Exception e) {
            log.error("YSFile invalid.", e);
            throw new BadRequestException("file invalid");
        }
        if (!YSFile.EXT_TF.equals(ysFile.getEXT())) {
            throw new BadRequestException("file EXT invalid");
        }
        return ysFile;
    }

}