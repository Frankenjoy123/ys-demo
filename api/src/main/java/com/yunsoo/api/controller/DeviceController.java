package com.yunsoo.api.controller;

import com.yunsoo.api.domain.DeviceDomain;
import com.yunsoo.api.dto.Device;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private RestClient dataAPIClient;

    @Autowired
    private DeviceDomain deviceDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'device:read')")
    public Device getDeviceById(@PathVariable(value = "id") String id) {
        DeviceObject object = dataAPIClient.get("device/{id}", DeviceObject.class, id);
        if (object == null) {
            throw new NotFoundException("device not found by [id: " + id + "]");
        }
        return toDevice(object);
    }

    @RequestMapping(value = "org/{orgid}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(#orgid, 'filterByOrg', 'device:read')")
    public List<Device> getDeviceByOrgId(@PathVariable(value = "orgid") String orgid,
                                         @PageableDefault(page = 0, size = 20)
                                         @SortDefault(value = "createdDateTime", direction = Sort.Direction.DESC)
                                         Pageable pageable,
                                         HttpServletResponse response) {

        if (!StringUtils.hasText(orgid))
            orgid = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();

        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append(pageable)
                .build();

        Page<List<DeviceObject>> deviceList = dataAPIClient.getPaged("device/org/" + orgid + query, new ParameterizedTypeReference<List<DeviceObject>>() {
        });

        response.setHeader("Content-Range", "pages " + deviceList.getPage() + "/" + deviceList.getTotal());

        Page<List<Device>> deivceResults = new Page<>(deviceList.getContent().stream()
                .map(this::toDevice)
                .collect(Collectors.toList()), deviceList.getPage(), deviceList.getTotal());

        return deivceResults.getContent();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#device.orgId, 'filterByOrg', 'device:create')")
    public String create(@RequestBody Device device) {
        DeviceObject object = toDeviceObject(device);
        object.setId(null);
        object.setCreatedDateTime(DateTime.now());
        String deviceId = dataAPIClient.post("device", object, String.class);
        return deviceId;
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#device, 'device:update')")
    public void update(@RequestBody Device device) {
        DeviceObject object = toDeviceObject(device);
        dataAPIClient.patch("device", object, DeviceObject.class);
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
            deviceNew = deviceDomain.update(deviceNew);
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
        device.setDeviceName(deviceObject.getName());
        device.setDeviceOs(deviceObject.getOs());
        device.setCheckPointId(deviceObject.getCheckPointId());
        device.setStatusCode(deviceObject.getStatusCode());
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
        deviceObject.setName(device.getDeviceName());
        deviceObject.setOs(device.getDeviceOs());
        deviceObject.setCheckPointId(device.getCheckPointId());
        deviceObject.setStatusCode(device.getStatusCode());
        deviceObject.setCreatedAccountId(device.getCreatedAccountId());
        deviceObject.setCreatedDateTime(device.getCreatedDateTime());
        deviceObject.setModifiedAccountId(device.getModifiedAccountId());
        deviceObject.setModifiedDatetime(device.getModifiedDatetime());
        return deviceObject;
    }
}
