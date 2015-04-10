package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.dto.basic.Account;
import com.yunsoo.api.rabbit.object.TAccount;
import com.yunsoo.api.rabbit.security.TokenAuthenticationService;
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

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> authUser(@RequestBody Account account) {
        if (account == null) {
            return new ResponseEntity<>("invalid user id", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        //Auth account
        Boolean authResult = true;
        //to-do

        if (!authResult) {
            return new ResponseEntity<>("用户名和密码未通过验证！", HttpStatus.UNAUTHORIZED);
        }
        // SecurityContextHolder.getContext().getAuthentication().isAuthenticated()

        //tokenAuthenticationService.getAuthentication()
        TAccount currentAccount = new TAccount();
        currentAccount.setId(2L);
        currentAccount.setUsername("admin");
        currentAccount.setOrgId(123456789L);
        String token = tokenAuthenticationService.generateToken(currentAccount);

        //set token
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER_NAME, token);
        return new ResponseEntity<String>(token, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
    public ResponseEntity<?> updatePassword(@RequestBody Account account) {
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}