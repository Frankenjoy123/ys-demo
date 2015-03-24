package com.yunsoo.dataapi.controller;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.dataapi.dto.ResultWrapper;
import com.yunsoo.dataapi.dto.UserDto;
import com.yunsoo.dataapi.factory.ResultFactory;
import com.yunsoo.common.data.object.FileObject;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.UserService;
import com.yunsoo.service.contract.User;
import com.yunsoo.config.AmazonSetting;
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
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") Long id) {
        UserDto userDto = UserDto.FromUser(userService.get(id));
        if (userDto == null) throw new ResourceNotFoundException("UserDto not found id=" + id);
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/cellular/{cellular}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserByCellular(@PathVariable(value = "cellular") String cellular) {
        return new ResponseEntity<UserDto>(UserDto.FromUser(userService.get(cellular)), HttpStatus.OK);
    }

    @RequestMapping(value = "/token/{devicecode}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByDeviceCode(@PathVariable(value = "devicecode") String devicecode) {
        //to-do
        List<User> users = userService.getUsersByFilter(null, devicecode, "", null);
        if (users == null || users.size() <= 0)
            throw new NotFoundException("Users not found token=" + devicecode);
        return new ResponseEntity<User>(users.get(0), HttpStatus.OK);
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
            fileObject.setLenth(s3Object.getObjectMetadata().getContentLength());
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
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) throws Exception {
        User user = UserDto.ToUser(userDto);
        long id = userService.save(user);
        HttpStatus status = id > 0L ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<Long>(id, status);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) throws Exception {
        //patch update, we don't provide functions like update with set null properties.
        User user = UserDto.ToUser(userDto);
        Boolean result = userService.patchUpdate(user).equals(ServiceOperationStatus.Success) ? true : false;
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id) {
        Boolean result = userService.delete(id); //deletePermanantly status is 5 in dev DB
        return new ResponseEntity<Boolean>(result, HttpStatus.NO_CONTENT);
    }
}
