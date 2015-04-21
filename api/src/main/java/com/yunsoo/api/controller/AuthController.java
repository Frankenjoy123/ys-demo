package com.yunsoo.api.controller;

import com.yunsoo.api.dto.basic.Account;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.security.TokenAuthenticationService;
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
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public ResponseEntity<String> grantRole(@RequestBody TAccount account) {
//        if (account == null) {
//            return new ResponseEntity<String>("invalid user id", HttpStatus.UNPROCESSABLE_ENTITY);
//        }
//
//        //to-do
//
//        //Fake token.
//        return new ResponseEntity<String>("abjksad8230.LK76-7J32-KLS34-7fDLK-09SDF", HttpStatus.OK);
//    }

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
        currentAccount.setId("552ddb49e6b1e79c80c950b8");
        currentAccount.setOrgId("123456789");
        currentAccount.setStatus(2); //Status的编码与TAccountStatusEnum一致
        String token = tokenAuthenticationService.generateToken(currentAccount);

        //set token
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER_NAME, token);
        return new ResponseEntity<String>(token, headers, HttpStatus.OK);
    }
}
