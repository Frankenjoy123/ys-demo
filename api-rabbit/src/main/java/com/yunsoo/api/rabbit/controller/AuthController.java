package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.Constants;
import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.dto.*;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.api.rabbit.security.UserAuthentication;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    /**
     * scenario 1. user find by phone, login with phone
     * scenario 2. anonymous user (phone is null) exists, register with phone
     * scenario 3. no user exists, create new user with phone
     *
     * @param deviceId     deviceId from RequestHeader
     * @param accessToken  accessToken from RequestHeader
     * @param loginRequest UserLoginRequest [phone must not be null or empty]
     * @return UserLoginResponse include user and access_token
     */
    @RequestMapping(value = "login/phone", method = RequestMethod.POST)
    public UserAuthResponse loginWithPhone(
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestHeader(value = Constants.HttpHeaderName.ACCESS_TOKEN, required = false) String accessToken,
            @RequestBody UserLoginRequest loginRequest) {
        //scenario 1. user find by phone, login with phone
        //find user by phone
        UserObject userObject = userDomain.getUserByPhone(loginRequest.getPhone());

        if (userObject == null) { //phone not found
            UserAuthentication userAuthentication = tokenAuthenticationService.getAuthentication(accessToken);
            if (userAuthentication != null) { //accessToken is valid
                //find user by accessToken
                userObject = userDomain.getUserById(userAuthentication.getDetails().getId());
                if (userObject.getPhone() == null) {
                    //scenario 2. anonymous user exists, register with phone
                    userObject.setPhone(loginRequest.getPhone());
                    if (!StringUtils.isEmpty(deviceId)) {
                        userObject.setDeviceId(deviceId);
                    }
                    userDomain.patchUpdateUser(userObject);
                }
            }

            if (userObject == null) {
                //scenario 3. no user exists, create new user with phone
                //create an new user
                userObject = new UserObject();
                userObject.setPhone(loginRequest.getPhone());
                userObject.setDeviceId(deviceId);
                userObject = userDomain.createUser(userObject);
            }

        }

        if (LookupCodes.UserStatus.DISABLED.equals(userObject.getStatusCode())) {
            throw new UnauthorizedException("user is disabled");
        }

        //update deviceId of the user
        if (deviceId != null && !deviceId.equals(userObject.getDeviceId())) {
            userObject.setDeviceId(deviceId);
            //create another object for patch update to avoid phone check logic
            UserObject newUserObject = new UserObject();
            newUserObject.setId(userObject.getId());
            newUserObject.setDeviceId(deviceId);
            userDomain.patchUpdateUser(newUserObject);
        }

        log.info(String.format("user login with phone [userId: %s]", userObject.getId()));

        //generate response
        return generateUserAuthResponse(userObject);
    }

    /**
     * 1. if access_token is invalid, register new user
     * 2. if access_token is valid, register phone to the exist user (phone is not null)
     *
     * @param deviceId        deviceId from RequestHeader
     * @param accessToken     accessToken from RequestHeader
     * @param registerRequest UserRegisterRequest
     * @return UserLoginResponse include user and access_token
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public UserAuthResponse registerUser(
            @RequestHeader(value = Constants.HttpHeaderName.DEVICE_ID, required = false) String deviceId,
            @RequestHeader(value = Constants.HttpHeaderName.ACCESS_TOKEN, required = false) String accessToken,
            @RequestBody(required = false) UserRegisterRequest registerRequest) {
        if (registerRequest == null) {
            registerRequest = new UserRegisterRequest();
        }

        UserObject userObject = registerRequest.toUserObject();
        userObject.setDeviceId(deviceId);

        if (StringUtils.isEmpty(accessToken)) {
            //register new user
            userObject = userDomain.createUser(userObject);

            log.info(String.format("new user created [userId: %s]", userObject.getId()));
        } else {
            //register phone to the exist user
            UserAuthentication userAuthentication = tokenAuthenticationService.getAuthentication(accessToken);
            if (userAuthentication == null) {
                throw new UnauthorizedException("access_token not valid");
            }
            userObject.setId(userAuthentication.getDetails().getId());
            userDomain.patchUpdateUser(userObject);
            userObject = userDomain.getUserById(userObject.getId());

            log.info(String.format("exist user registered [userId: %s]", userObject.getId()));
        }

        //generate response
        return generateUserAuthResponse(userObject);
    }

    private UserAuthResponse generateUserAuthResponse(UserObject userObject) {
        Token accessToken = tokenAuthenticationService.generateAccessToken(userObject.getId());
        UserAuthResponse loginResponse = new UserAuthResponse();
        loginResponse.setUser(new User(userObject));
        loginResponse.setAccessToken(accessToken);
        return loginResponse;
    }

}