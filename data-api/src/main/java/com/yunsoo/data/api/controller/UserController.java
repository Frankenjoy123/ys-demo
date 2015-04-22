package com.yunsoo.data.api.controller;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.data.api.dto.UserDto;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.data.service.service.ServiceOperationStatus;
import com.yunsoo.data.service.service.UserService;
import com.yunsoo.data.service.service.contract.User;
import com.yunsoo.data.service.config.AmazonSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
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
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") String id) {
        User user = userService.getById(id);
        if (user == null) throw new NotFoundException("User not found for id = " + id);
        UserDto userDto = UserDto.FromUser(user);
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/cellular/{cellular}", method = RequestMethod.GET)
    public UserDto getUserByCellular(@PathVariable(value = "cellular") String cellular) {
        User user = userService.getByCellular(cellular);
        if (user == null) throw new NotFoundException("User not found for cellular = " + cellular);
        return UserDto.FromUser(user);
    }

    @RequestMapping(value = "/device/{devicecode}", method = RequestMethod.GET)
    public User getUserByDeviceCode(@PathVariable(value = "devicecode") String devicecode) {
        List<User> users = userService.getUsersByFilter(null, devicecode, "", null);
        if (users == null || users.size() <= 0)
            throw new NotFoundException("Users not found token=" + devicecode);
        return users.get(0);
    }

    @RequestMapping(value = "/thumbnail/{id}/{key}", method = RequestMethod.GET)
    public ResponseEntity getThumbnail(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "key") String key) {

        if (key == null || key.isEmpty()) throw new BadRequestException("Key不能为空！");
        S3Object s3Object;
        try {
            s3Object = userService.getUserThumbnail(amazonSetting.getS3_basebucket(), amazonSetting.getS3_userbaseurl() + "/" + id + "/" + key);
            if (s3Object == null) throw new NotFoundException("找不到图片!");

            FileObject fileObject = new FileObject();
            fileObject.setSuffix(s3Object.getObjectMetadata().getContentType());
            fileObject.setThumbnailData(IOUtils.toByteArray(s3Object.getObjectContent()));
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
    public String createUser(@RequestBody UserDto userDto) throws Exception {
        User user = UserDto.ToUser(userDto);
        String id = userService.save(user);
        return id;
//        HttpStatus status = (id.length() > 20) ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
//        return new ResponseEntity<String>(id, status);
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public void updateUser(@RequestBody UserDto userDto) throws Exception {
        //patch update, we don't provide functions like update with set null properties.
        User user = UserDto.ToUser(userDto);
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
}
