package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountTokenDomain;
import com.yunsoo.api.domain.DeviceDomain;
import com.yunsoo.api.dto.Device;
import com.yunsoo.api.security.AuthDetails;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.AccountTokenObject;
import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Autowired
    private AccountTokenDomain accountTokenDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'device:read')")
    public Device getById(@PathVariable(value = "id") String id) {
        DeviceObject deviceObject = deviceDomain.getById(id);
        if (deviceObject == null) {
            throw new NotFoundException("device not found by [id: " + id + "]");
        }
        return new Device(deviceObject);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'device:read')")
    public List<Device> getByFilterPaged(
            @RequestParam(value = "org_id", required = false) String orgId,
            @RequestParam(value = "login_account_id", required = false) String accountId,
            @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
            Pageable pageable,
            HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId);

        Page<DeviceObject> devicePage = deviceDomain.getByFilterPaged(orgId, accountId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", devicePage.toContentRange());
        }
        return devicePage.map(Device::new).getContent();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void update(@PathVariable(value = "id") String id, @RequestBody Device device) {
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
        deviceDomain.patchUpdate(deviceCurrent);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @PreAuthorize("hasPermission('current', 'org', 'device:write')")
    public Device register(@RequestBody Device device) {
        DeviceObject deviceNew = device.toDeviceObject();
        String accountId = AuthUtils.getCurrentAccount().getId();
        String orgId = AuthUtils.getCurrentAccount().getOrgId();

        List<AccountTokenObject> tokens = accountTokenDomain.getNonExpiredByDeviceId(deviceNew.getId());
        if (tokens.size() == 0) {
            throw new BadRequestException("device can not be registered without permanent_token");
        }
        deviceNew.setOrgId(orgId);
        deviceNew.setLoginAccountId(accountId);
        deviceNew.setStatusCode(LookupCodes.DeviceStatus.ACTIVATED);
        deviceNew.setCreatedAccountId(accountId);
        deviceNew.setCreatedDateTime(DateTime.now());
        deviceDomain.put(deviceNew);
        return new Device(deviceNew);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unregister(@PathVariable(value = "id") String id) {
        DeviceObject deviceCurrent = deviceDomain.getById(id);
        if (deviceCurrent != null) {
            AuthUtils.checkPermission(deviceCurrent.getOrgId(), "device", "delete");
            accountTokenDomain.expirePermanentTokenByDeviceId(id);
            deviceDomain.delete(id);
        }
    }


    @RequestMapping(value = "log", method = RequestMethod.POST)
    public void uploadInBody(@RequestParam(value = "file_name", required = false) String fileName,
                             HttpServletRequest request) throws IOException {
        AuthDetails authDetails = AuthUtils.getAuthDetails();
        String appId = authDetails.getAppId();
        String deviceId = authDetails.getDeviceId();
        if (StringUtils.isEmpty(deviceId)) {
            throw new BadRequestException("device_id invalid");
        }

        if (!StringUtils.hasText(fileName)) {
            fileName = DateTime.now().toString("yyyyMMdd");
        } else {
            fileName = fileName.replace("/", "");
        }
        deviceDomain.uploadLog(appId, deviceId, fileName, request.getContentLength(), request.getInputStream());

    }

}
