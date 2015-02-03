package com.yunsoo.controller;

import com.yunsoo.service.ServiceOperationStatus;
import com.yunsoo.service.UserService;
import com.yunsoo.service.contract.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public User getUserById(@PathVariable(value = "id") Long id) {
        return userService.get(id);
    }

    @RequestMapping(value = "/token/{deviceCode}", method = RequestMethod.GET)
    public User getUserByDeviceCode(@PathVariable(value = "deviceCode") String deviceCode) {
        //to-do
        List<User> users = userService.getUsersByFilter(null, deviceCode, "", null);
        if (users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }

    @RequestMapping(value = "/nearby/{location}", method = RequestMethod.GET)
    public User getUserByLocation(@PathVariable(value = "location") String location) {
        //to-do
        return null;
    }

    //Return -1L if Fail, or the userId if Success.
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public long createUser(User user) {
        long id = userService.save(user);
        return id;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public boolean updateUser(User user) {
        ServiceOperationStatus serviceOperationStatus = userService.update(user);
        if (serviceOperationStatus.equals(ServiceOperationStatus.Success)) return true;
        else return false;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public boolean deleteUser(@PathVariable(value = "id") Long id) {
        return userService.delete(id, 5); //delete status is 5 in dev DB
    }

}
