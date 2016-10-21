package com.yunsoo.auth.api.controller;

import com.yunsoo.auth.dto.OAuthAccount;
import com.yunsoo.auth.service.OAuthAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public List<OAuthAccount> getOauthAccountList(@RequestParam("source_list") List<String> sourceList, @RequestParam("source_type") String sourceType) {
        if (sourceList.size() == 0) {
            return new ArrayList<>();
        }
        return oAuthAccountService.search(sourceList, sourceType);
    }

    @RequestMapping(method = RequestMethod.GET, value = "count")
    public int count(@RequestParam("source_list") List<String> sourceList, @RequestParam("source_type") String sourceType) {
        if (sourceList.size() == 0) {
            return 0;
        }
        return oAuthAccountService.count(sourceList, sourceType);
    }


}
