package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.config.AWSConfigProperties;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import com.yunsoo.data.service.service.UserService;
import com.yunsoo.data.service.service.contract.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/1/26
 * Descriptions:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AWSConfigProperties awsConfigProperties;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserObject getUserById(@PathVariable(value = "id") String id) {
        User user = userService.getById(id);
        if (user == null) throw new NotFoundException("User not found for id = " + id);
        return this.FromUser(user);
    }

    @RequestMapping(value = "/cellular/{cellular}", method = RequestMethod.GET)
    public UserObject getUserByCellular(@PathVariable(value = "cellular") String cellular) {
        User user = userService.getByCellular(cellular);
        if (user == null) throw new NotFoundException("User not found for cellular = " + cellular);
        return this.FromUser(user);
    }

    @RequestMapping(value = "/device/{devicecode}", method = RequestMethod.GET)
    public UserObject getUserByDeviceCode(@PathVariable(value = "devicecode") String devicecode) {
        List<User> users = userService.getUsersByFilter(null, devicecode, "", null);
        if (users == null || users.size() <= 0)
            throw new NotFoundException("Users not found token=" + devicecode);
        return this.FromUser(users.get(0));
    }

    @RequestMapping(value = "/{id}/gravatar/{name}", method = RequestMethod.GET)
    public ResponseEntity getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "name") String name) {

        S3Object s3Object;
        try {
            s3Object = userService.getUserThumbnail(awsConfigProperties.getS3().getBucketName(),
                    "user/" + id + "/gravatar/" + name);
            if (s3Object == null) throw new NotFoundException("image not found");

            FileObject fileObject = new FileObject();
            fileObject.setContentType(s3Object.getObjectMetadata().getContentType());
            fileObject.setData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLength(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<FileObject>(fileObject, HttpStatus.OK);
        } catch (IOException ex) {
            throw new InternalServerErrorException();
        }
    }

    @RequestMapping(value = "/nearby/{location}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByLocation(@PathVariable(value = "location") String location) {
        //to-do
        return null;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String createUser(@RequestBody UserObject userObject) throws Exception {
        User user = this.ToUser(userObject);
        return userService.save(user);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateUser(@RequestBody UserObject userObject) throws Exception {
        //patch update, we don't provide functions like update with set null properties.
        User user = this.ToUser(userObject);
        ServiceOperationStatus result = userService.patchUpdate(user);

        if (result == ServiceOperationStatus.ObjectNotFound) {
            throw new NotFoundException("未找到相关用户记录！");
        } else if (result == ServiceOperationStatus.Fail) {
            throw new InternalServerErrorException("更新用户失败！");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "id") String id) {
        userService.delete(id); //deletePermanantly status is 5 in dev DB
    }

    private UserObject FromUser(User User) {
        UserObject userObject = new UserObject();
        BeanUtils.copyProperties(User, userObject);
        return userObject;
    }

    private User ToUser(UserObject userObject) {
        User user = new User();
        BeanUtils.copyProperties(userObject, user);
        //convert thumbnail information into FileObject if exists
        if (userObject.getThumbnailData() != null) {
            FileObject fileObject = new FileObject();
            fileObject.setData(userObject.getThumbnailData());
            fileObject.setSuffix(userObject.getThumbnailSuffix());
            if (userObject.getThumbnailContentLength() != null) {
                fileObject.setLength(userObject.getThumbnailContentLength());
            } else {
                fileObject.setLength((long) userObject.getThumbnailData().length);
            }
            user.setFileObject(fileObject);
        }
        return user;
    }

    private List<UserObject> FromUserList(List<User> userList) {
        if (userList == null) return null;

        List<UserObject> userObjectList = new ArrayList<>();
        for (User user : userList) {
            userObjectList.add(this.FromUser(user));
        }
        return userObjectList;
    }

    private List<User> ToUserList(List<UserObject> userObjectList) {
        if (userObjectList == null) return null;

        List<User> userList = new ArrayList<>();
        for (UserObject userObject : userObjectList) {
            userList.add(this.ToUser(userObject));
        }
        return userList;
    }
}
