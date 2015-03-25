package com.yunsoo.api.controller;

import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TAccountToken;
import com.yunsoo.api.security.AccountAuthentication;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zhe on 2015/3/5.
 * Handle with accounts which consumes this API.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private RestClient dataAPIClient;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public TAccount getCurrent() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AccountAuthentication) {
            return ((AccountAuthentication) authentication).getDetails();
        }
        return new TAccount(authentication.getName()); //anonymous user support
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<TAccount> list() {
        //return userRepository.findAll();
        return null;
    }

    @RequestMapping(value = "/generateToken", method = RequestMethod.POST)
    public TAccountToken generateToken(@RequestBody TAccount account) {
        String identifier = account.getUsername();
        TAccountToken token = dataAPIClient.get("/accountToken/identifier/{identifier}", TAccountToken.class, identifier);
        if (token != null) {
            dataAPIClient.put("/accountToken/update", token);
            return dataAPIClient.get("/accountToken/identifier/{identifier}", TAccountToken.class, identifier);
}

        return dataAPIClient.post("/accountToken/create", account, TAccountToken.class);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public TAccountToken login(@RequestBody TAccount account) {
        Boolean result = dataAPIClient.post("/account/verify", account, Boolean.class);
        if (result) {
            return this.generateToken(account);
        }
        return null;
    }
}