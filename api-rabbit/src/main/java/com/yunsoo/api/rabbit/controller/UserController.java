package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserActivityDomain;
import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.dto.User;
import com.yunsoo.api.rabbit.object.TUser;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


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

    @Autowired
    private UserActivityDomain userActivityDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable(value = "id") String userId) {
        userId = fixUserId(userId);
        UserObject user = userDomain.getUserById(userId);
        if (user == null) {
            throw new NotFoundException(40401, "user not found by [id:" + userId + "]");
        }
        return new User(user);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    @PreAuthorize("hasPermission(#userId, 'userId', 'user:modify')")
    public void patchUpdateUser(@PathVariable(value = "id") String userId,
                                @RequestBody User user) {
        userId = fixUserId(userId);
        UserObject userObject = user.toUserObject();
        userObject.setId(userId);
        userDomain.patchUpdateUser(userObject);
    }

    @RequestMapping(value = "{id}/gravatar", method = RequestMethod.GET)
    public ResponseEntity<?> getUserGravatar(@PathVariable(value = "id") String userId,
                                             @RequestParam(value = "image_name", required = false) String imageName) {
        userId = fixUserId(userId);
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

    @RequestMapping(value = "{id}/gravatar", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#userId, 'userId', 'user:modify')")
    public void saveUserGravatar(@PathVariable(value = "id") String userId,
                                 @RequestBody byte[] imageDataBytes) {
        if (imageDataBytes != null && imageDataBytes.length > 0) {
            userId = fixUserId(userId);
            userDomain.saveUserGravatar(userId, imageDataBytes);
        }
    }

    @RequestMapping(value = "{id}/signin/continuousdays", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#userId, 'userId', 'user:read')")
    public int getSignInContinuousDays(@PathVariable(value = "id") String userId) {
        userId = fixUserId(userId);
        return userActivityDomain.getSignInContinuousDays(userId);
    }

    @RequestMapping(value = "{id}/signin", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#userId, 'userId', 'user:modify')")
    public int signIn(@PathVariable(value = "id") String userId) {
        userId = fixUserId(userId);
        return userActivityDomain.signIn(userId);
    }


    private String fixUserId(String userId) {
        if (userId == null || "current".equals(userId)) {
            //current orgId
            TUser currentUser = tokenAuthenticationService.getAuthentication().getDetails();
            return currentUser == null ? null : currentUser.getId();
        }
        return userId;
    }

}
