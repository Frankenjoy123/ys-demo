package com.yunsoo.controller;

import com.yunsoo.service.UserService;
import com.yunsoo.service.contract.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/id", method = RequestMethod.GET)
    public User getUserById(@PathVariable(value = "id") Integer id) {
        return userService.get(id);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public User getUserByToken(@PathVariable(value = "token") String token) {
        //to-do
        return null;
    }

    @RequestMapping(value = "/nearby", method = RequestMethod.GET)
    public User getUserByLocation(@PathVariable(value = "location") String location) {
        //to-do
        return null;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createUser(User user) {
        //to-do
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.PUT)
    public void deleteUser() {
        //to-do
    }

}
