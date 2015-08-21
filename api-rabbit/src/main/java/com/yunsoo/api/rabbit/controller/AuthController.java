package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.dto.User;
import com.yunsoo.api.rabbit.dto.UserResult;
import com.yunsoo.api.rabbit.object.Constants;
import com.yunsoo.api.rabbit.object.TAccount;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.web.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Zhe on 2015/3/5.
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
    public ResponseEntity<?> authUser(@RequestHeader(value = Constants.HttpHeaderName.ACCESS_TOKEN, required = false) String accessToken,
                                      @RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>("用户不能为空！", HttpStatus.FORBIDDEN);
        }
        if (StringUtils.isEmpty(user.getDeviceId())) {
            throw new BadRequestException("device_id不能为空！");
        }

        User currentUser = null;
        if (!StringUtils.isEmpty(accessToken)) {
            TAccount tAccount = tokenAuthenticationService.parseUser(accessToken);
            //get user id from token. check if cellular exists, and update user
            currentUser = userDomain.ensureUser(tAccount.getId(), user.getDeviceId(), user.getPhone());
        } else {
            currentUser = userDomain.ensureUser(null, user.getDeviceId(), user.getPhone());
        }

        TAccount currentAccount = new TAccount();
        currentAccount.setId(currentUser.getId());
        currentAccount.setStatus(currentUser.getStatusCode()); //Status的编码与TAccountStatusEnum一致
        String token = tokenAuthenticationService.generateToken(currentAccount, false);

        //set token
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.HttpHeaderName.ACCESS_TOKEN, token);
        UserResult userResult = new UserResult(token, currentUser.getId()); //generate result
        return new ResponseEntity<UserResult>(userResult, headers, HttpStatus.OK);
    }

    //Always create new anonymous user.
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@RequestBody User user) throws Exception {
        //always create new user.
        User currentUser = userDomain.createAnonymousUser(user.getDeviceId());

        TAccount currentAccount = new TAccount();
        currentAccount.setId(currentUser.getId());
        currentAccount.setStatus(currentUser.getStatusCode());  //Status的编码与TAccountStatusEnum一致  TAccountStatusEnum.ENABLED.value()
        String token = tokenAuthenticationService.generateToken(currentAccount, true); //stay long

        //set token
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.HttpHeaderName.ACCESS_TOKEN, token);
        UserResult userResult = new UserResult(token, currentUser.getId()); //generate result
        return new ResponseEntity<UserResult>(userResult, headers, HttpStatus.CREATED);
    }

}