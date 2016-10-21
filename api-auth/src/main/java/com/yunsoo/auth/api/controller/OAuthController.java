package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.dto.OAuthAccount;
import com.yunsoo.auth.service.OAuthAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-10-18
 * Descriptions:
 */
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    private OAuthAccountService service;

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

    @RequestMapping(method = RequestMethod.GET)
    public List<OAuthAccount> getOauthAccountList(@RequestParam("source_list") List<String> sourceList, @RequestParam("source_type") String sourceType){
        if(sourceList.size() == 0)
            return new ArrayList<>();
        return service.search(sourceList, sourceType);
    }

    @RequestMapping(method = RequestMethod.GET, value = "count")
    public int count(@RequestParam("source_list") List<String> sourceList, @RequestParam("source_type") String sourceType) {
        if(sourceList.size() == 0)
            return 0;
        return service.count(sourceList, sourceType);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public OAuthAccount getById(@PathVariable("id") String id){
        return service.getById(id);
    }


}
