package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.dto.basic.Account;
import com.yunsoo.api.rabbit.dto.basic.User;
import com.yunsoo.api.rabbit.object.TAccount;
import com.yunsoo.api.rabbit.object.TAccountStatusEnum;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
import com.yunsoo.common.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Zhe on 2015/3/5.
 * //Wait for Kaibing to implements it.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${yunsoo.token_header_name}")
    private String AUTH_HEADER_NAME;
    //    private static final long HALF_YEAR = 1000 * 60 * 60 * 24 * 150; // 150 days
    @Autowired
    private RestClient dataAPIClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> authUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>("用户不能为空！", HttpStatus.FORBIDDEN);
        }

        //Auth account
        Boolean authResult = true;
        User currentUser = null;
        //几种验证方法： 1， 先验证手机号存在， 2，验证devicecode存在， 3， 用户名/密码验证（未做）
        if (user.getDeviceCode() != null && !user.getDeviceCode().isEmpty()) {
            currentUser = dataAPIClient.get("user/token/{devicecode}", User.class, user.getDeviceCode());
            if (currentUser == null) {
                return new ResponseEntity<>("用户名不存在！", HttpStatus.FORBIDDEN);
            }
        } else if (user.getCellular() != null && !user.getCellular().isEmpty()) {
            currentUser = dataAPIClient.get("user/cellular/{cellular}", User.class, user.getCellular());
            if (currentUser == null) {
                return new ResponseEntity<>("用户名不存在！", HttpStatus.FORBIDDEN);
            }
        } else {
            authResult = false;
        }

        if (!authResult) {
            return new ResponseEntity<>("用户名和密码未通过验证！", HttpStatus.UNAUTHORIZED);
        }

        TAccount currentAccount = new TAccount();
        currentAccount.setId(currentUser.getId());
        currentAccount.setStatus(currentUser.getStatus()); //Status的编码与TAccountStatusEnum一致
        String token = tokenAuthenticationService.generateToken(currentAccount);

        //set token
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER_NAME, token);
        return new ResponseEntity<String>(token, headers, HttpStatus.OK);
    }

    //Allow anonymous access
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@RequestBody User user) throws Exception {
        //Reset user's default creadit and level
        user.setYsCreadit(0);
        user.setLevel(1);
        String id = dataAPIClient.post("user", user, String.class);

        TAccount currentAccount = new TAccount();
        currentAccount.setId(id);
        currentAccount.setStatus(TAccountStatusEnum.ENABLED.value()); //Status的编码与TAccountStatusEnum一致
        String token = tokenAuthenticationService.generateToken(currentAccount);

        //set token
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER_NAME, token);
        return new ResponseEntity<String>(id, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
    public ResponseEntity<?> updatePassword(@RequestBody Account account) {
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}