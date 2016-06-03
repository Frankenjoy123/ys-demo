package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.TaskFileEntryObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.TaskFileEntryEntity;
import com.yunsoo.data.service.repository.TaskFileEntryRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-05-31
 * Descriptions:
 */
@RestController
@RequestMapping("/taskFileEntry")
public class TaskFileEntryController {

    @Autowired
    private TaskFileEntryRepository taskFileEntryRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public TaskFileEntryObject getById(@PathVariable("id") String fileId) {
        TaskFileEntryEntity entity = taskFileEntryRepository.findOne(fileId);
        if (entity == null) {
            throw new NotFoundException("taskFileEntry not found by [id: " + fileId + "]");
        }
        return toTaskFileEntryObject(entity);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TaskFileEntryObject> getByFilter(
            @RequestParam(value = "org_id", required = true) String orgId,
            @RequestParam(value = "app_id", required = false) String appId,
            @RequestParam(value = "device_id", required = false) String deviceId,
            @RequestParam(value = "type_code", required = false) String typeCode,
            @RequestParam(value = "status_code_in", required = false) List<String> statusCodeIn,
            @RequestParam(value = "created_account_id", required = false) String createdAccountId,
            @RequestParam(value = "created_datetime_start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeStart,
            @RequestParam(value = "created_datetime_end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime createdDateTimeEnd,
            Pageable pageable,
            HttpServletResponse response) {
        Page<TaskFileEntryEntity> entityPage = taskFileEntryRepository.query(
                orgId,
                appId,
                deviceId,
                typeCode,
                statusCodeIn == null || statusCodeIn.size() == 0 ? null : statusCodeIn,
                statusCodeIn == null || statusCodeIn.size() == 0,
                createdAccountId,
                createdDateTimeStart,
                createdDateTimeEnd,
                pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages(), (int) entityPage.getTotalElements()));
        }
        return entityPage.getContent().stream().map(this::toTaskFileEntryObject).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public TaskFileEntryObject create(@RequestBody @Valid TaskFileEntryObject obj) {
        TaskFileEntryEntity entity = toTaskFileEntryEntity(obj);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        entity = taskFileEntryRepository.save(entity);
        return toTaskFileEntryObject(entity);
    }

    @RequestMapping(value = "{id}/statusCode", method = RequestMethod.PUT)
    public void updateStatusCode(@PathVariable("id") String fileId, @RequestBody String statusCode) {
        TaskFileEntryEntity entity = taskFileEntryRepository.findOne(fileId);
        if (entity == null) {
            throw new NotFoundException("taskFileEntry not found by [id: " + fileId + "]");
        }
        entity.setStatusCode(statusCode);
        taskFileEntryRepository.save(entity);
    }


    private TaskFileEntryObject toTaskFileEntryObject(TaskFileEntryEntity entity) {
        if (entity == null) {
            return null;
        }
        TaskFileEntryObject obj = new TaskFileEntryObject();
        obj.setFileId(entity.getFileId());
        obj.setOrgId(entity.getOrgId());
        obj.setAppId(entity.getAppId());
        obj.setDeviceId(entity.getDeviceId());
        obj.setName(entity.getName());
        obj.setTypeCode(entity.getTypeCode());
        obj.setStatusCode(entity.getStatusCode());
        obj.setCreatedAccountId(entity.getCreatedAccountId());
        obj.setCreatedDateTime(entity.getCreatedDateTime());
        return obj;
    }

    private TaskFileEntryEntity toTaskFileEntryEntity(TaskFileEntryObject obj) {
        if (obj == null) {
            return null;
        }
        TaskFileEntryEntity entity = new TaskFileEntryEntity();
        entity.setFileId(obj.getFileId());
        entity.setOrgId(obj.getOrgId());
        entity.setAppId(obj.getAppId());
        entity.setDeviceId(obj.getDeviceId());
        entity.setName(obj.getName());
        entity.setTypeCode(obj.getTypeCode());
        entity.setStatusCode(obj.getStatusCode());
        entity.setCreatedAccountId(obj.getCreatedAccountId());
        entity.setCreatedDateTime(obj.getCreatedDateTime());
        return entity;
    }

}
