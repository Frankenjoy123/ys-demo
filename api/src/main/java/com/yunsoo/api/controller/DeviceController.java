package com.yunsoo.api.controller;

import com.yunsoo.api.dto.Device;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.DeviceObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
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
 * Created by Zhe on 2015/5/11.
 */

@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'device:read')")
    public Device getDeviceById(@PathVariable(value = "id") String id) {
        DeviceObject object = dataAPIClient.get("device/{id}", DeviceObject.class, id);
        if (object == null) {
            throw new NotFoundException("device not found by [id: " + id + "]");
        }
        return Device.fromDeviceObject(object);
    }

    @RequestMapping(value = "/org/{orgid}", method = RequestMethod.GET)
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
                .map(Device::fromDeviceObject)
                .collect(Collectors.toList()), deviceList.getPage(), deviceList.getTotal());

        return deivceResults.getContent();
    }

    @RequestMapping(value = "/whose/{id}", method = RequestMethod.GET)
//    @PostAuthorize("hasPermission(#id, 'filterByOrg', 'device:read')")
    public List<Device> getDeviceByAccountId(@PathVariable(value = "id") String id,
                                             @RequestParam(value = "index", required = false) Integer index,
                                             @RequestParam(value = "size", required = false) Integer size) {
        List<DeviceObject> deviceList = dataAPIClient.get("device/whose/{id}?index={1}&size={2}",
                new ParameterizedTypeReference<List<DeviceObject>>() {
                }, id, index, size);
        return Device.fromDeviceObjectList(deviceList);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#device.orgId, 'filterByOrg', 'device:create')")
    public String create(@RequestBody Device device) {
        DeviceObject object = Device.toDeviceObject(device);
        object.setId(null);
        String deviceId = dataAPIClient.post("device", object, String.class);
        return deviceId;
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#device, 'device:update')")
    public void update(@RequestBody Device device) {
        DeviceObject object = Device.toDeviceObject(device);
        dataAPIClient.patch("device", object, DeviceObject.class);
    }
}
