package com.yunsoo.controller;

import com.yunsoo.service.ProductCategoryService;
import com.yunsoo.service.UserService;
import com.yunsoo.service.contract.ProductCategory;
import com.yunsoo.service.contract.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public User getUserById(@RequestParam(value = "id", required = true) Integer id) {
        return userService.get(id);
    }

}
