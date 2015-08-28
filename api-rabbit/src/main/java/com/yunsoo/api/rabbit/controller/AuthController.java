package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.dto.*;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.api.rabbit.security.UserAuthentication;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.UnauthorizedException;
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

    /**
     * login with phone, phone must have been registered already
     *
     * @param deviceId     deviceId from RequestHeader
     * @param loginRequest UserLoginRequest
     * @param response     current HttpServletResponse
     * @return UserLoginResponse include user and access_token
     */
    @RequestMapping(value = "login/phone", method = RequestMethod.POST)
    public UserLoginResponse loginWithPhone(
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestBody UserLoginRequest loginRequest,
            HttpServletResponse response) {
        if (!StringUtils.isEmpty(loginRequest.getDeviceId())) {
            deviceId = loginRequest.getDeviceId();
        }

        //check user
        UserObject userObject = userDomain.getUserByPhone(loginRequest.getPhone());
        if (userObject == null) {
            throw new UnauthorizedException("phone not exists");
        }
        if (LookupCodes.UserStatus.DISABLED.equals(userObject.getStatusCode())) {
            throw new UnauthorizedException("user is disabled");
        }

        //update deviceId of the user
        if (deviceId != null && !deviceId.equals(userObject.getDeviceId())) {
            UserObject newUserObject = new UserObject();
            newUserObject.setId(userObject.getId());
            newUserObject.setDeviceId(deviceId);
            userDomain.patchUpdateUser(newUserObject);
        }

        LOGGER.info("user login with phone [userId: {}]", userObject.getId());

        //generate response
        Token accessToken = tokenAuthenticationService.generateAccessToken(userObject.getId());
        UserLoginResponse loginResponse = new UserLoginResponse();
        loginResponse.setUser(new User(userObject));
        loginResponse.setAccessToken(accessToken);

        //set token to the response header
        response.setHeader(Constants.HttpHeaderName.ACCESS_TOKEN, accessToken.getToken());

        return loginResponse;
    }

    /**
     * 1. if access_token is null, register new user
     * 2. if access_token is available, register phone to the exist user (phone is not null)
     *
     * @param deviceId        deviceId from RequestHeader
     * @param accessToken     accessToken from RequestHeader
     * @param registerRequest UserRegisterRequest
     * @param response        current HttpServletResponse
     * @return UserLoginResponse include user and access_token
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public UserLoginResponse registerUser(
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestHeader(value = Constants.HttpHeaderName.ACCESS_TOKEN, required = false) String accessToken,
            @RequestBody(required = false) UserRegisterRequest registerRequest,
            HttpServletResponse response) {
        if (registerRequest == null) {
            registerRequest = new UserRegisterRequest();
        }
        if (StringUtils.isEmpty(registerRequest.getDeviceId())) {
            registerRequest.setDeviceId(deviceId);
        }
        UserObject userObject = registerRequest.toUserObject();

        if (StringUtils.isEmpty(accessToken)) {
            //register new user
            userObject = userDomain.createUser(userObject);

            LOGGER.info("new user registered [userId: {}]", userObject.getId());
        } else {
            //register phone to the exist user
            UserAuthentication userAuthentication = tokenAuthenticationService.getAuthentication(accessToken);
            if (userAuthentication == null) {
                throw new UnauthorizedException("access_token not valid");
            }
            userObject.setId(userAuthentication.getDetails().getId());
            userDomain.patchUpdateUser(userObject);
            userObject = userDomain.getUserById(userObject.getId());

            LOGGER.info("exist user registered [userId: {}]", userObject.getId());
        }

        //generate response
        Token newAccessToken = tokenAuthenticationService.generateAccessToken(userObject.getId());
        UserLoginResponse loginResponse = new UserLoginResponse();
        loginResponse.setUser(new User(userObject));
        loginResponse.setAccessToken(newAccessToken);

        //set token to the response header
        response.setHeader(Constants.HttpHeaderName.ACCESS_TOKEN, newAccessToken.getToken());

        return loginResponse;
    }


    @Deprecated
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

    @Deprecated
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