package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.dto.User;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by:   Zhe
 * Created on:   2015/3/3
 * Descriptions: This controller manage end user.
 * Only authorized user can consume it.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDomain userDomain;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#id, 'User', 'user:read')")
    public User getById(@PathVariable(value = "id") String id) {
        UserObject user = userDomain.getUserById(id);
        if (user == null) {
            throw new NotFoundException(40401, "User not found by [id:" + id + "]");
        }
        return new User(user);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> getByFilter(@RequestParam(value = "phone", required = false) String phone,
                                  @RequestParam(value = "device_id", required = false) String deviceId) {
        List<UserObject> users;
        if (!StringUtils.isEmpty(phone)) {
            users = userDomain.getUsersByPhone(phone);
        } else if (!StringUtils.isEmpty(deviceId)) {
            users = userDomain.getUsersByDeviceId(deviceId);
        } else {
            throw new BadRequestException("at least need one filter parameter of phone or device_id");
        }
        return users.stream().map(User::new).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#userId, 'User', 'user:modify')")
    public void updateUser(@PathVariable(value = "id") String userId,
                           @RequestBody User user) {
        UserObject userObject = user.toUserObject();
        userObject.setId(userId);
        userDomain.patchUpdateUser(userObject);
    }

    @RequestMapping(value = "{id}/gravatar/{image_name}", method = RequestMethod.GET)
    public ResponseEntity<?> getGravatar(@PathVariable(value = "id") String userId,
                                         @PathVariable(value = "image_name") String imageName) {
        ResourceInputStream resourceInputStream = userDomain.getUserGravatar(userId, imageName);
        if (resourceInputStream == null) {
            throw new NotFoundException("gravatar not found");
        }
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(resourceInputStream.getContentType()));
        if (resourceInputStream.getContentLength() > 0) {
            builder.contentLength(resourceInputStream.getContentLength());
        }
        return builder.body(new InputStreamResource(resourceInputStream));
    }

    @RequestMapping(value = "{id}/gravatar/{image_name}", method = RequestMethod.PUT)
    public void saveGravatar(@PathVariable(value = "id") String userId,
                             @PathVariable(value = "image_name") String imageName,
                             @RequestBody byte[] imageDataBytes) {
        if (imageDataBytes != null && imageDataBytes.length > 0) {
            userDomain.saveUserGravatar(userId, imageName, imageDataBytes);
        }
    }


    @Deprecated
    @RequestMapping(value = "/cellular/{cellular}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#token, 'authenticated')")
    public User getByPhone(@PathVariable(value = "cellular") String cellular) {
        List<User> users = getByFilter(cellular, null);
        if (users.size() == 0) {
            throw new NotFoundException("user not found by cellular");
        }
        return users.get(0);
    }

    @Deprecated
    @RequestMapping(value = "/device/{deviceId}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#token, 'authenticated')")
    public User getByDeviceId(@PathVariable(value = "deviceId") String deviceId) {
        List<User> users = getByFilter(deviceId, null);
        if (users.size() == 0) {
            throw new NotFoundException("user not found by cellular");
        }
        return users.get(0);
    }

    @Deprecated
    @RequestMapping(value = "/{id}/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> getThumbnail(
            @PathVariable(value = "id") String userId,
            @PathVariable(value = "name") String name) {
        return getGravatar(userId, "image-400x400");
    }


//    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasPermission(#user, 'authenticated')")
//    public void deleteUser(
//            @PathVariable(value = "userId") String userId) throws Exception {
//        dataAPIClient.delete("user/{id}", userId);
//    }
}
