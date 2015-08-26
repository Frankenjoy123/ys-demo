package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.dto.User;
import com.yunsoo.api.rabbit.dto.UserResult;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.api.rabbit.security.UserAuthentication;
import com.yunsoo.common.web.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by:   Zhe
 * Created on:   2015/3/5
 * Descriptions:
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDomain userDomain;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public UserResult authUser(
            @RequestHeader(value = Constants.HttpHeaderName.OLD_ACCESS_TOKEN, required = false) String accessToken,
            @RequestBody User user,
            HttpServletResponse response) {
        if (StringUtils.isEmpty(user.getDeviceId())) {
            throw new BadRequestException("device_id不能为空！");
        }

        User currentUser = null;
        if (!StringUtils.isEmpty(accessToken)) {
            UserAuthentication userAuthentication = tokenAuthenticationService.getAuthentication(accessToken);
            if (userAuthentication != null) {
                //get user id from token. check if cellular exists, and update user
                currentUser = userDomain.ensureUser(userAuthentication.getDetails().getId(), user.getDeviceId(), user.getPhone());
            } else {
                currentUser = userDomain.ensureUser(null, user.getDeviceId(), user.getPhone());
            }
        } else {
            currentUser = userDomain.ensureUser(null, user.getDeviceId(), user.getPhone());
        }

        String token = tokenAuthenticationService.generateAccessToken(currentUser.getId()).getToken();

        //set token
        response.setHeader(Constants.HttpHeaderName.OLD_ACCESS_TOKEN, token);

        return new UserResult(token, currentUser.getId());
    }

    //Always create new anonymous user.
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResult createUser(@RequestBody User user,
                                 HttpServletResponse response) throws Exception {
        //always create new user.
        User currentUser = userDomain.createAnonymousUser(user.getDeviceId());

        String token = tokenAuthenticationService.generateAccessToken(currentUser.getId()).getToken();

        response.setHeader(Constants.HttpHeaderName.OLD_ACCESS_TOKEN, token);

        return new UserResult(token, currentUser.getId());
    }

}