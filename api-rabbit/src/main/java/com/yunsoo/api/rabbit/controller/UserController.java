package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.dto.basic.User;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;


/**
 * Created by:   Zhe
 * Created on:   2015/3/3
 * Descriptions: This controller manage end user.
 * Only authorized user can consume it.
 * <p>
 * ErrorCode
 * 40401    :   User not found!
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RestClient dataAPIClient;
//    private final String AUTH_HEADER_NAME = "YS_RABBIT_AUTH_TOKEN";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'User', 'user:read')")
    public User getById(@PathVariable(value = "id") String id) throws NotFoundException {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("UserId不应为空！");
        }
        User user = dataAPIClient.get("user/id/{id}", User.class, id);
        if (user == null) throw new NotFoundException(40401, "User not found id=" + id);
        return user;
    }

    @RequestMapping(value = "/cellular/{cellular}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#token, 'authenticated')")
    public User getByCellular(@PathVariable(value = "cellular") String cellular) throws NotFoundException {
        if (cellular == null || cellular.isEmpty()) {
            throw new BadRequestException("cellular不能为空！");
        }
        User user = dataAPIClient.get("user/cellular/{cellular}", User.class, cellular);
        if (user == null) throw new NotFoundException(40401, "User not found cellular=" + cellular);
        return user;
    }

    @RequestMapping(value = "/device/{devicecode}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#token, 'authenticated')")
    public User getByDevicecode(@PathVariable(value = "devicecode") String deviceCode) throws NotFoundException {
        if (deviceCode == null || deviceCode.isEmpty()) {
            throw new BadRequestException("deviceCode不能为空！");
        }
        User user = null;
        try {
            user = dataAPIClient.get("user/device/{devicecode}", User.class, deviceCode);
            return user;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "User not found by device code=" + deviceCode);
        }
    }

    @RequestMapping(value = "/{id}/{key}", method = RequestMethod.GET)
//    @PreAuthorize("hasPermission(#user, 'user:read')")
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "key") String key) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("Id不应为空！");
        }
        if (key == null || key.isEmpty()) {
            throw new BadRequestException("Key不应为空！");
        }

        FileObject fileObject = dataAPIClient.get("user/{id}/{key}", FileObject.class, id, key);
        if (fileObject.getLength() > 0) {
            return ResponseEntity.ok()
                    .contentLength(fileObject.getLength())
                    .contentType(MediaType.parseMediaType(fileObject.getContentType()))
                    .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileObject.getContentType()))
                    .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getData())));
        }
    }


    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#user, 'authenticated')")
    public void updateUser(
            @RequestBody User user) throws Exception {
        dataAPIClient.patch("user", user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#user, 'authenticated')")
    public void deleteUser(
            @PathVariable(value = "userId") String userId) throws Exception {
        dataAPIClient.delete("user/{id}", userId);
    }
}
