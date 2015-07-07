package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.service.config.AmazonSetting;
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
 * Created by Zhe on 2015/1/26.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private AmazonSetting amazonSetting;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserObject> getUserById(@PathVariable(value = "id") String id) {
        User user = userService.getById(id);
        if (user == null) throw new NotFoundException("User not found for id = " + id);
        UserObject userObject = this.FromUser(user);
        return new ResponseEntity<UserObject>(userObject, HttpStatus.OK);
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

    @RequestMapping(value = "/{id}/{key}", method = RequestMethod.GET)
    public ResponseEntity getThumbnail(
            @PathVariable(value = "id") String id,
            @PathVariable(value = "key") String key) {

        if (key == null || key.isEmpty()) throw new BadRequestException("Key不能为空！");
        S3Object s3Object;
        try {
            s3Object = userService.getUserThumbnail(amazonSetting.getS3_basebucket(), amazonSetting.getS3_userbaseurl() + "/" + id + "/" + key);
            if (s3Object == null) throw new NotFoundException("找不到图片!");

            FileObject fileObject = new FileObject();
//            fileObject.setSuffix(s3Object.getObjectMetadata().getContentType());
            fileObject.setContentType(s3Object.getObjectMetadata().getContentType());
            fileObject.setData(IOUtils.toByteArray(s3Object.getObjectContent()));
            fileObject.setLength(s3Object.getObjectMetadata().getContentLength());
            return new ResponseEntity<FileObject>(fileObject, HttpStatus.OK);

        } catch (IOException ex) {
            //to-do: log
            throw new InternalServerErrorException("图片获取出错！");
        }
    }

    @RequestMapping(value = "/nearby/{location}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByLocation(@PathVariable(value = "location") String location) {
        //to-do
        return null;
    }

    //Return -1L if Fail, or the userId if Success.
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String createUser(@RequestBody UserObject userObject) throws Exception {
        User user = this.ToUser(userObject);
        String id = userService.save(user);
        return id;
//        HttpStatus status = (id.length() > 20) ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
//        return new ResponseEntity<String>(id, status);
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
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") String id) {
        Boolean result = userService.delete(id); //deletePermanantly status is 5 in dev DB
        return new ResponseEntity<Boolean>(result, HttpStatus.NO_CONTENT);
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
