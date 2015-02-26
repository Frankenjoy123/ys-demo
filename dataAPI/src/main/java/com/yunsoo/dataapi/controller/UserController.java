package com.yunsoo.dataapi.controller;

import com.yunsoo.dataapi.dto.ResultWrapper;
import com.yunsoo.dataapi.factory.ResultFactory;
import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.UserService;
import com.yunsoo.service.contract.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<User>(userService.get(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/cellular/{cellular}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByCellular(@PathVariable(value = "cellular") String cellular) {
        return new ResponseEntity<User>(userService.get(cellular), HttpStatus.OK);
    }

    @RequestMapping(value = "/token/{deviceCode}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByDeviceCode(@PathVariable(value = "deviceCode") String deviceCode) {
        //to-do
        List<User> users = userService.getUsersByFilter(null, deviceCode, "", null);
        return new ResponseEntity<User>(users.get(0), HttpStatus.OK);
//        if (users.size() == 0) {
//            return new ResponseEntity<User>(null, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<User>(users.get(0), HttpStatus.OK);
//        }
    }

    @RequestMapping(value = "/nearby/{location}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByLocation(@PathVariable(value = "location") String location) {
        //to-do
        return null;
    }

    //Return -1L if Fail, or the userId if Success.
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ResultWrapper> createUser(@RequestBody User user) {
        long id = userService.save(user);
        HttpStatus status = id > 0L ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(id), status);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<ResultWrapper> updateUser(@RequestBody User user) {
        Boolean result = userService.update(user).equals(ServiceOperationStatus.Success) ? true : false;
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResultWrapper> deleteUser(@PathVariable(value = "id") Long id) {
        Boolean result = userService.delete(id, 5); //delete status is 5 in dev DB
        return new ResponseEntity<ResultWrapper>(ResultFactory.CreateResult(result), HttpStatus.NO_CONTENT);
    }

}
