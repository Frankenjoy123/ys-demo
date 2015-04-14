package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.dto.basic.User;
import com.yunsoo.api.rabbit.object.TAccount;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.security.Principal;


/**
 * Created by:   Zhe
 * Created on:   2015/3/3
 * Descriptions: This controller manage end user.
 *               Only authorized user can consume it.
 * <p>
 * ErrorCode
 * 40401    :   User not found!
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    UserController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#user, 'user:read')")
    public User getById(@PathVariable(value = "id") Integer id) throws NotFoundException {
        if (id == null || id < 0) {
            throw new BadRequestException("UserId不应小于0！");
        }
        User user = dataAPIClient.get("user/id/{id}", User.class, id);
        if (user == null) throw new NotFoundException(40401, "User not found id=" + id);
        return user;
    }

    @RequestMapping(value = "/cellular/{cellular}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#user, 'user:read')")
//    @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, 'admin')")
    public User getByCellular(@PathVariable(value = "cellular") Long cellular) throws NotFoundException {
        if (cellular == null || cellular <= 0L) {
            throw new BadRequestException("cellular格式错误！");
        }
        User user = dataAPIClient.get("user/cellular/{cellular}", User.class, cellular);
        if (user == null) throw new NotFoundException(40401, "User not found cellular=" + cellular);
        return user;
    }

    @RequestMapping(value = "/device/{devicecode}", method = RequestMethod.GET)
//    @PreAuthorize("hasAnyRole('COM_USER','YUNSOO_ADMIN')")
    @PreAuthorize("hasPermission(#user, 'user:read')")
    public User getByDevicecode(@PathVariable(value = "devicecode") String deviceCode) throws NotFoundException {
        User user = null;
        try {
            user = dataAPIClient.get("user/token/{devicecode}", User.class, deviceCode);
            return user;
        } catch (NotFoundException ex) {
            throw new NotFoundException(40401, "User not found by device code=" + deviceCode);
        }
    }

    @RequestMapping(value = "/thumbnail/{id}/{key}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#user, 'user:read')")
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "key") String key) {
        if (id == null || id < 0) {
            throw new BadRequestException("Id不应小于0！");
        }
        if (key == null || key.isEmpty()) {
            throw new BadRequestException("Key不应为空！");
        }

        FileObject fileObject = dataAPIClient.get("user/thumbnail/{id}/{key}", FileObject.class, id, key);
        if (fileObject.getLenth() > 0) {
            return ResponseEntity.ok()
                    .contentLength(fileObject.getLenth())
                    .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                    .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getThumbnailData())));
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileObject.getSuffix()))
                    .body(new InputStreamResource(new ByteArrayInputStream(fileObject.getThumbnailData())));
        }
    }


    @RequestMapping(value = "", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#user, 'user:update')")
    public void updateUser(@RequestBody User user) throws Exception {
        dataAPIClient.patch("user/update", user);
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasPermission(#user, 'user:delete')")
    public void deleteUser(@PathVariable(value = "userId") Long userId) throws Exception {
        dataAPIClient.delete("user/delete/{id}", userId);
    }
}
