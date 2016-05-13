package com.yunsoo.data.api.controller;

import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.PageableUtils;
import com.yunsoo.data.service.entity.DeviceEntity;
import com.yunsoo.data.service.repository.DeviceRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            Pageable pageable,
            HttpServletResponse response) {
        Page<DeviceEntity> entityPage;
        if (StringUtils.hasText(orgId)) {
            if (StringUtils.hasText(accountId)) {
                entityPage = deviceRepository.findByOrgIdAndLoginAccountId(orgId, accountId, pageable);
            } else {
                entityPage = deviceRepository.findByOrgId(orgId, pageable);
            }
        } else {
            entityPage = deviceRepository.findAll(pageable);
        }
        if (pageable != null) {
            response.setHeader("Content-Range", PageableUtils.formatPages(entityPage.getNumber(), entityPage.getTotalPages()));
        }
        return StreamSupport.stream(entityPage.spliterator(), false)
                .map(this::toDeviceObject)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void save(@PathVariable(value = "id") String id, @RequestBody @Valid DeviceObject deviceObject) {
        DeviceEntity entity = toDeviceEntity(deviceObject);
        entity.setId(id);
        if (entity.getCreatedDateTime() == null) {
            entity.setCreatedDateTime(DateTime.now());
        }
        if (entity.getModifiedDatetime() == null) {
            entity.setModifiedDatetime(DateTime.now());
        }
        deviceRepository.save(entity);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdate(@PathVariable(value = "id") String id, @RequestBody @Valid DeviceObject deviceObject) {
        DeviceEntity entity = findById(id);
        if (deviceObject.getName() != null) {
            entity.setName(deviceObject.getName());
        }
        if (deviceObject.getOs() != null) {
            entity.setOs(deviceObject.getOs());
        }
        if (deviceObject.getStatusCode() != null) {
            entity.setStatusCode(deviceObject.getStatusCode());
        }
        if (deviceObject.getCheckPointId() != null) {
            entity.setCheckPointId(deviceObject.getCheckPointId());
        }
        if (deviceObject.getComments() != null) {
            entity.setComments(deviceObject.getComments());
        }
        if (deviceObject.getModifiedAccountId() != null) {
            entity.setModifiedAccountId(deviceObject.getModifiedAccountId());
        }
        if (deviceObject.getModifiedDatetime() != null) {
            entity.setModifiedDatetime(deviceObject.getModifiedDatetime());
        }
        deviceRepository.save(entity);
    }

    private DeviceEntity findById(String id) {
        DeviceEntity entity = deviceRepository.findOne(id);
        if (entity == null) {
            throw new NotFoundException("device not found by id [id: " + id + "]");
        }
        return entity;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(value = "id") String id) {
        if (deviceRepository.findOne(id) != null) {
            deviceRepository.delete(id);
        }
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
