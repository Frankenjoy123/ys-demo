package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.TaskFileDomain;
import com.yunsoo.api.dto.TaskFileEntry;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.TaskFileEntryObject;
import com.yunsoo.common.support.YSFile;
import com.yunsoo.common.util.FileNameUtils;
import com.yunsoo.common.util.StringFormatter;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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

        if (pageable != null) {
            response.setHeader("Content-Range", page.toContentRange());
        }
        return page.getContent().stream().map(TaskFileEntry::new).collect(Collectors.toList());
    }


    @RequestMapping(value = "form", method = RequestMethod.POST)
    public TaskFileEntry uploadInForm(@RequestParam("file") MultipartFile file,
                                      @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
                                      @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException();
        }
        log.info("received file in form " + StringFormatter.formatMap("size", file.getSize(), "appId", appId, "deviceId", deviceId));
        YSFile ysFile = readYSFile(file.getInputStream());

        TaskFileEntryObject obj = taskFileDomain.saveYSFile(FileNameUtils.shorten(file.getOriginalFilename(), FILENAME_LENGTH_LIMIT), ysFile, appId, deviceId);
        return new TaskFileEntry(obj);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public TaskFileEntry uploadInBody(@RequestParam(value = "file_name", required = false) String fileName,
                                      @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
                                      @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
                                      HttpServletRequest request) throws IOException {
        log.info("received file in request body "
                + StringFormatter.formatMap("size", request.getContentLengthLong(), "appId", appId, "deviceId", deviceId));
        YSFile ysFile = readYSFile(request.getInputStream());

        TaskFileEntryObject obj = taskFileDomain.saveYSFile(FileNameUtils.shorten(fileName, FILENAME_LENGTH_LIMIT), ysFile, appId, deviceId);
        return new TaskFileEntry(obj);
    }


    @RequestMapping(value = "count", method = RequestMethod.GET)
    public long[] getTotalTaskFiles(@RequestParam(value = "device_id", required = false) String deviceId,
                                  @RequestParam(value = "type_code", required = false) String typeCode,
                                  @RequestParam(value = "created_datetime_start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime start,
                                  @RequestParam(value = "created_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime end){

        String orgId = AuthUtils.fixOrgId(null);
        return new long[]{1, 1};
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