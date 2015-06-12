package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.entity.DeviceEntity;
import com.yunsoo.data.service.repository.DeviceRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Jerry on 3/13/2015.
 * Updated by Zhe on 5/11/2015.
 * Updated by Lijian on 6/12/2015.
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceRepository deviceRepository;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DeviceObject getById(@PathVariable(value = "id") String id) {
        return toDeviceObject(findById(id));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<DeviceObject> getByFilterPaged(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "login_account_id", required = false) String accountId,
            @PageableDefault(size = 1000)
            Pageable pageable,
            HttpServletResponse response) {
        Page<DeviceEntity> entities;
        if (StringUtils.hasText(accountId)) {
            entities = deviceRepository.findByLoginAccountId(accountId, pageable);
        } else if (StringUtils.hasText(orgId)) {
            entities = deviceRepository.findByOrgId(orgId, pageable);
        } else {
            entities = deviceRepository.findAll(pageable);
        }

        response.setHeader("Content-Range", "pages " + entities.getNumber() + "/" + entities.getTotalPages());

        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::toDeviceObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceObject create(@Valid @RequestBody DeviceObject deviceObject) {
        DeviceEntity entity = toDeviceEntity(deviceObject);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        return toDeviceObject(deviceRepository.save(entity));
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void update(@RequestBody DeviceObject deviceObject) {
        DeviceEntity entityOld = findById(deviceObject.getId());
        DeviceEntity entity = toDeviceEntity(deviceObject);

        entity.setCreatedAccountId(entityOld.getCreatedAccountId());
        entity.setCreatedDateTime(entityOld.getCreatedDateTime());
        if (entity.getModifiedDatetime() == null) {
            entity.setModifiedDatetime(DateTime.now());
        }
        deviceRepository.save(entity);
    }

    private DeviceEntity findById(String id) {
        DeviceEntity entity = deviceRepository.findById(id);
        if (entity == null) {
            throw new NotFoundException("device not found by id [id: " + id + "]");
        }
        return entity;
    }

    private DeviceEntity toDeviceEntity(DeviceObject deviceObject) {
        if (deviceObject == null) {
            return null;
        }
        DeviceEntity entity = new DeviceEntity();
        entity.setId(deviceObject.getId());
        entity.setOrgId(deviceObject.getOrgId());
        entity.setLoginAccountId(deviceObject.getLoginAccountId());
        entity.setName(deviceObject.getName());
        entity.setOs(deviceObject.getOs());
        entity.setCheckPointId(deviceObject.getCheckPointId());
        entity.setStatusCode(deviceObject.getStatusCode());
        entity.setComments(deviceObject.getComments());
        entity.setCreatedAccountId(deviceObject.getCreatedAccountId());
        entity.setCreatedDateTime(deviceObject.getCreatedDateTime());
        entity.setModifiedAccountId(deviceObject.getModifiedAccountId());
        entity.setModifiedDatetime(deviceObject.getModifiedDatetime());
        return entity;
    }

    private DeviceObject toDeviceObject(DeviceEntity entity) {
        if (entity == null) {
            return null;
        }
        DeviceObject deviceObject = new DeviceObject();
        deviceObject.setId(entity.getId());
        deviceObject.setOrgId(entity.getOrgId());
        deviceObject.setLoginAccountId(entity.getLoginAccountId());
        deviceObject.setName(entity.getName());
        deviceObject.setOs(entity.getOs());
        deviceObject.setCheckPointId(entity.getCheckPointId());
        deviceObject.setStatusCode(entity.getStatusCode());
        deviceObject.setComments(entity.getComments());
        deviceObject.setCreatedAccountId(entity.getCreatedAccountId());
        deviceObject.setCreatedDateTime(entity.getCreatedDateTime());
        deviceObject.setModifiedAccountId(entity.getModifiedAccountId());
        deviceObject.setModifiedDatetime(entity.getModifiedDatetime());
        return deviceObject;
    }

}
