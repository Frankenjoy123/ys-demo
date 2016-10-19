package com.yunsoo.auth.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Lijian
 * Created on:   2016-10-18
 * Descriptions:
 */
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    public void login() {

    }

    public void bind() {


    }

    public String getToken() {
        return null;
    }

    @RequestMapping("accessToken")
    public String getAccessToken(@RequestParam("oauth_account_id") String oAuthAccountId,
                                 @RequestParam("token") String token) {
        return null;
    }


}
