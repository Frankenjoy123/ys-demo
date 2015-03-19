package com.yunsoo.api.controller;

import com.yunsoo.api.dto.basic.User;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by:   Zhe
 * Created on:   2015/3/3
 * Descriptions: This controller manage end user.
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
    @PreAuthorize("hasRole('YUNSOO_ADMIN')")
    public User getById(@PathVariable(value = "id") int id) throws NotFoundException {
        User user = dataAPIClient.get("user/id/{id}", User.class, id);
        if (user == null) throw new NotFoundException("User not found id=" + id);
        return user;
    }

    @RequestMapping(value = "/cellular/{cellular}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#user, 'getUserById')")
//    @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, 'admin')")
    public User getByCellular(@PathVariable(value = "cellular") int cellular) throws NotFoundException {
        User user = dataAPIClient.get("user/cellular/{cellular}", User.class, cellular);
        if (user == null) throw new NotFoundException("User not found cellular=" + cellular);
        return user;
    }


    @RequestMapping(value = "/token/{devicecode}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('COM_USER','YUNSOO_ADMIN')")
//    @PreAuthorize("hasPermission(#contact, 'admin')")
    public User getByCellular(@PathVariable(value = "devicecode") String deviceCode) throws NotFoundException {
        User user = dataAPIClient.get("user/token/{devicecode}", User.class, deviceCode);
        if (user == null) throw new NotFoundException("User not found token=" + deviceCode);
        return user;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) throws Exception {
        long id = dataAPIClient.post("user/create", user, Long.class);
        User resultUser = new User();
        resultUser.setId(id);
        return resultUser;
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public void updateUser(@RequestBody User user) throws Exception {
        dataAPIClient.patch("user/update", user);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestBody Integer userId) throws Exception {
        dataAPIClient.delete("user/delete/{id}", userId);
    }
}
