package com.yunsoo.api.controller;

import com.yunsoo.api.domain.DeviceDomain;
import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.dto.Device;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by  : Zhe
 * Created on  : 2015/5/11
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private PermissionDomain permissionDomain;

    @Autowired
    private DeviceDomain deviceDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'device:read')")
    public Device getById(@PathVariable(value = "id") String id) {
        DeviceObject deviceObject = deviceDomain.getById(id);
        if (deviceObject == null) {
            throw new NotFoundException("device not found by [id: " + id + "]");
        }
        return toDevice(deviceObject);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(#orgId, 'filterByOrg', 'device:read')")
    public List<Device> getByFilterPaged(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "login_account_id", required = false) String accountId,
            @PageableDefault(page = 0, size = 20)
            @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
            Pageable pageable,
            HttpServletResponse response) {

        if (!StringUtils.hasText(orgId)) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }

        Page<List<DeviceObject>> devices = deviceDomain.getByFilterPaged(orgId, accountId, pageable);

        response.setHeader("Content-Range", "pages " + devices.getPage() + "/" + devices.getTotal());

        return devices.getContent().stream().map(this::toDevice).collect(Collectors.toList());
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void update(@RequestBody Device device) {
        String id = device.getId();
        String accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        DeviceObject deviceCurrent = deviceDomain.getById(id);
        if (deviceCurrent == null) {
            throw new NotFoundException("device not found by [id: " + id + "]");
        }
        if (!permissionDomain.hasPermission(accountId, new TPermission(deviceCurrent.getOrgId(), "device", "update"))) {
            throw new ForbiddenException();
        }

        if (device.getCheckPointId() != null) deviceCurrent.setCheckPointId(device.getCheckPointId());
        if (device.getName() != null) deviceCurrent.setName(device.getName());
        if (device.getComments() != null) deviceCurrent.setComments(device.getComments());
        if (device.getStatusCode() != null) deviceCurrent.setStatusCode(device.getStatusCode());

        deviceCurrent.setModifiedAccountId(accountId);
        deviceCurrent.setModifiedDatetime(DateTime.now());
        deviceDomain.update(deviceCurrent);
    }


    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Device register(@RequestBody Device device) {
        DeviceObject deviceNew = toDeviceObject(device);
        String accountId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        String orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        deviceNew.setOrgId(orgId);
        deviceNew.setStatusCode(LookupCodes.DeviceStatus.ACTIVATED);

        DeviceObject deviceCurrent = deviceDomain.getById(deviceNew.getId());
        if (deviceCurrent == null) {
            //create device
            if (deviceNew.getCreatedAccountId() == null) {
                deviceNew.setCreatedAccountId(accountId);
            }
            deviceNew.setCreatedDateTime(DateTime.now());
            deviceNew.setModifiedAccountId(null);
            deviceNew.setModifiedDatetime(null);
            deviceNew = deviceDomain.create(deviceNew);
        } else {
            //update device
            deviceCurrent.setOrgId(deviceNew.getOrgId());
            deviceCurrent.setStatusCode(deviceNew.getStatusCode());
            deviceCurrent.setModifiedAccountId(deviceNew.getModifiedAccountId() != null ? deviceNew.getModifiedAccountId() : accountId);
            deviceCurrent.setModifiedDatetime(DateTime.now());
            if (deviceNew.getName() != null) {
                deviceCurrent.setName(deviceNew.getName());
            }
            if (deviceNew.getOs() != null) {
                deviceCurrent.setOs(deviceNew.getOs());
            }
            if (deviceNew.getCheckPointId() != null) {
                deviceCurrent.setCheckPointId(deviceNew.getCheckPointId());
            }
            deviceDomain.update(deviceNew);
        }
        return toDevice(deviceNew);
    }

    private Device toDevice(DeviceObject deviceObject) {
        if (deviceObject == null) {
            return null;
        }
        Device device = new Device();
        device.setId(deviceObject.getId());
        device.setOrgId(deviceObject.getOrgId());
        device.setLoginAccountId(deviceObject.getLoginAccountId());
        device.setName(deviceObject.getName());
        device.setOs(deviceObject.getOs());
        device.setCheckPointId(deviceObject.getCheckPointId());
        device.setStatusCode(deviceObject.getStatusCode());
        device.setComments(deviceObject.getComments());
        device.setCreatedAccountId(deviceObject.getCreatedAccountId());
        device.setCreatedDateTime(deviceObject.getCreatedDateTime());
        device.setModifiedAccountId(deviceObject.getModifiedAccountId());
        device.setModifiedDatetime(deviceObject.getModifiedDatetime());
        return device;
    }

    private DeviceObject toDeviceObject(Device device) {
        if (device == null) {
            return null;
        }
        DeviceObject deviceObject = new DeviceObject();
        deviceObject.setId(device.getId());
        deviceObject.setOrgId(device.getOrgId());
        deviceObject.setLoginAccountId(device.getLoginAccountId());
        deviceObject.setName(device.getName());
        deviceObject.setOs(device.getOs());
        deviceObject.setCheckPointId(device.getCheckPointId());
        deviceObject.setStatusCode(device.getStatusCode());
        deviceObject.setComments(device.getComments());
        deviceObject.setCreatedAccountId(device.getCreatedAccountId());
        deviceObject.setCreatedDateTime(device.getCreatedDateTime());
        deviceObject.setModifiedAccountId(device.getModifiedAccountId());
        deviceObject.setModifiedDatetime(device.getModifiedDatetime());
        return deviceObject;
    }
}
