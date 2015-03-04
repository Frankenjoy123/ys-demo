package com.yunsoo.api.controller;

import com.yunsoo.api.data.RestClient;
import com.yunsoo.api.dto.basic.User;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by:   Zhe
 * Created on:   2015/3/3
 * Descriptions:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private RestClient dataAPIClient;

    @Autowired
    UserController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getById(@PathVariable(value = "id") int id) throws NotFoundException {
        User user = dataAPIClient.get("user/id/{id}", User.class, id);
        if (user == null) throw new NotFoundException("User not found id=" + id);
        return user;
    }

    @RequestMapping(value = "cellular/{cellular}", method = RequestMethod.GET)
    public User getByCellular(@PathVariable(value = "cellular") int cellular) throws NotFoundException {
        User user = dataAPIClient.get("user/cellular/{cellular}", User.class, cellular);
        if (user == null) throw new NotFoundException("User not found cellular=" + cellular);
        return user;
    }

    @RequestMapping(value = "token/{deviceCode}", method = RequestMethod.GET)
    public User getByCellular(@PathVariable(value = "deviceCode") String deviceCode) throws NotFoundException {
        User user = dataAPIClient.get("user/token/{deviceCode}", User.class, deviceCode);
        if (user == null) throw new NotFoundException("User not found token=" + deviceCode);
        return user;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long createUser(@RequestBody User user) throws Exception {
        long id = -1;
        return id;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Boolean updateUser(@RequestBody User user) throws Exception {
        Boolean result = false;
        return result;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Boolean deleteUser(@RequestBody User user) throws Exception {
        Boolean result = false;
        return result;
    }
}
