package com.yunsoo.api.controller;

import com.yunsoo.api.domain.DeviceDomain;
import com.yunsoo.api.dto.Device;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by  : Zhe
 * Created on  : 2015/5/11
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private DeviceDomain deviceDomain;

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
    @PostAuthorize("hasPermission(#orgId, 'org', 'device:read')")
    public List<Device> getByFilterPaged(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "login_account_id", required = false) String accountId,
            @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
            Pageable pageable,
            HttpServletResponse response) {

        if (!StringUtils.hasText(orgId)) {
            orgId = AuthUtils.getCurrentAccount().getOrgId();
        }

        Page<DeviceObject> devicePage = deviceDomain.getByFilterPaged(orgId, accountId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", devicePage.toContentRange());
        }
        return devicePage.map(this::toDevice).getContent();
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PostAuthorize("hasPermission(#device, 'device:write')")
    public void update(@RequestBody Device device) {
        String id = device.getId();
        String accountId = AuthUtils.getCurrentAccount().getId();
        DeviceObject deviceCurrent = deviceDomain.getById(id);
        if (deviceCurrent == null) {
            throw new NotFoundException("device not found by [id: " + id + "]");
        }
        AuthUtils.checkPermission(deviceCurrent.getOrgId(), "device", "write");

        if (device.getCheckPointId() != null) deviceCurrent.setCheckPointId(device.getCheckPointId());
        if (device.getName() != null) deviceCurrent.setName(device.getName());
        if (device.getComments() != null) deviceCurrent.setComments(device.getComments());
        if (device.getStatusCode() != null) deviceCurrent.setStatusCode(device.getStatusCode());

        deviceCurrent.setModifiedAccountId(accountId);
        deviceCurrent.setModifiedDatetime(DateTime.now());
        deviceDomain.update(deviceCurrent);
    }


    @RequestMapping(value = "register", method = RequestMethod.POST)
    @PostAuthorize("hasPermission(#device, 'device:create')")
    public Device register(@RequestBody Device device) {
        DeviceObject deviceNew = toDeviceObject(device);
        String accountId = AuthUtils.getCurrentAccount().getId();
        String orgId = AuthUtils.getCurrentAccount().getOrgId();
        deviceNew.setOrgId(orgId);
        deviceNew.setLoginAccountId(accountId);
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
            deviceNew.setLoginAccountId(deviceNew.getLoginAccountId());
            deviceCurrent.setStatusCode(deviceNew.getStatusCode());
            deviceCurrent.setModifiedAccountId(deviceNew.getModifiedAccountId() != null ? deviceNew.getModifiedAccountId() : accountId);
            deviceCurrent.setModifiedDatetime(DateTime.now());
            if (deviceNew.getName() != null) {
                deviceCurrent.setName(deviceNew.getName());
            }
            if (deviceNew.getOs() != null) {
                deviceCurrent.setOs(deviceNew.getOs());
            }
            if (deviceNew.getComments() != null) {
                deviceCurrent.setComments(deviceNew.getComments());
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
