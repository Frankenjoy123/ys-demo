package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.api.security.AuthDetails;
import com.yunsoo.auth.api.util.AuthUtils;
import com.yunsoo.auth.dto.HeartBeatPackage;
import com.yunsoo.auth.service.HeartBeatService;
import com.yunsoo.common.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-07-25
 * Descriptions:
 */
@RestController
@RequestMapping("/heartbeat")
public class HeartbeatController {

    @Autowired
    private HeartBeatService heartBeatService;


    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('current', 'org', 'device:read')")
    public HeartBeatPackage getByDeviceId(@RequestParam("device_id") String deviceId) {
        return heartBeatService.getBeat(deviceId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HeartBeatPackage beat() {
        AuthDetails authDetails = AuthUtils.getAuthDetails();
        String appId = authDetails.getAppId();
        String deviceId = authDetails.getDeviceId();
        if (StringUtils.isEmpty(deviceId)) {
            throw new BadRequestException("device_id invalid");
        }
        return heartBeatService.beat(deviceId, appId);
    }

}
