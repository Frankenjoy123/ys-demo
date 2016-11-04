package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.dto.OAuthAccount;
import com.yunsoo.auth.service.OAuthAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-10-21
 * Descriptions:
 */
@RestController
@RequestMapping("/oauth/account")
public class OAuthAccountController {

    @Autowired
    private OAuthAccountService oAuthAccountService;

    @RequestMapping(method = RequestMethod.GET)
    public List<OAuthAccount> getOauthAccountList(@RequestParam(value = "source_list", required = false) List<String> sourceList,
                                                  @RequestParam(value = "source_type", required = false) String sourceType,
                                                  @RequestParam(value = "account_id", required = false) String accountId) {


        return oAuthAccountService.search(sourceList, sourceType, accountId);
    }



    @RequestMapping(method = RequestMethod.GET, value = "count")
    public int count(@RequestParam("source_list") List<String> sourceList, @RequestParam("source_type") String sourceType) {
        if (sourceList.size() == 0) {
            return 0;
        }
        return oAuthAccountService.count(sourceList, sourceType);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public OAuthAccount getById(@PathVariable("id") String id){
        return oAuthAccountService.getById(id);
    }



}
