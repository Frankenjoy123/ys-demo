package com.yunsoo.api.controller;

import com.yunsoo.api.auth.dto.Device;
import com.yunsoo.api.auth.service.AuthDeviceService;
import com.yunsoo.api.file.service.FileService;
import com.yunsoo.api.security.AuthDetails;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;

/**
 * Created by  : Zhe
 * Created on  : 2015/5/11
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/device")
public class DeviceController {

    @Autowired
    private FileService fileService;

    @Autowired
    private AuthDeviceService authDeviceService;

    @RequestMapping(value = "log", method = RequestMethod.POST)
    public void uploadInBody(@RequestParam(value = "file_name", required = false) String fileName,
                             HttpServletRequest request) throws IOException {
        AuthDetails authDetails = AuthUtils.getAuthDetails();
        String appId = authDetails.getAppId();
        String deviceId = authDetails.getDeviceId();
        if (StringUtils.isEmpty(deviceId)) {
            throw new BadRequestException("device_id invalid");
        }

        Device device = authDeviceService.getById(deviceId);
        if (device == null) {
            throw new BadRequestException("device_id invalid");
        }

        if (!StringUtils.hasText(fileName)) {
            fileName = DateTime.now().toString("yyyyMMdd");
        } else {
            fileName = fileName.replace("/", "");
        }

        String s3FileName =  String.format("application/%s/device/%s/log/%s",appId, deviceId, fileName);
        ResourceInputStream stream = new ResourceInputStream(request.getInputStream(), request.getContentLength(), MediaType.TEXT_PLAIN_VALUE);
        fileService.putFile(s3FileName, stream);

    }

}
