package com.yunsoo.dataapi.controller;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.yunsoo.dataapi.dto.ResultWrapper;
import com.yunsoo.dataapi.dto.UserDto;
import com.yunsoo.dataapi.factory.ResultFactory;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.UserService;
import com.yunsoo.service.contract.User;
import com.yunsoo.util.SpringBeanUtil;
import org.springframework.beans.BeanUtils;
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
            throw new ResourceNotFoundException("Users not found token=" + devicecode);
        return new ResponseEntity<User>(users.get(0), HttpStatus.OK);
    }

    @RequestMapping(value = "/nearby/{location}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByLocation(@PathVariable(value = "location") String location) {
        //to-do
        return null;
    }

    //Return -1L if Fail, or the userId if Success.
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ResultWrapper> createUser(@RequestBody UserDto userDto) throws Exception {
        User user = UserDto.ToUser(userDto);
        long id = userService.save(user);
        HttpStatus status = id > 0L ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(id), status);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public ResponseEntity<ResultWrapper> updateUser(@RequestBody UserDto userDto) throws Exception {
        //patch update, we don't provide functions like update with set null properties.
        User user = UserDto.ToUser(userDto);
        Boolean result = userService.patchUpdate(user).equals(ServiceOperationStatus.Success) ? true : false;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultWrapper> deleteUser(@PathVariable(value = "id") Long id) {
        Boolean result = userService.delete(id); //delete status is 5 in dev DB
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.NO_CONTENT);
    }
}
