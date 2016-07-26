package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.Constants;
import com.yunsoo.auth.dto.HeartBeatPackage;
import com.yunsoo.auth.service.HeartBeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public HeartBeatPackage beat(
            @RequestHeader(value = Constants.HttpHeaderName.APP_ID) String appId,
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID) String deviceId) {
        return heartBeatService.beat(deviceId, appId);
    }

}
