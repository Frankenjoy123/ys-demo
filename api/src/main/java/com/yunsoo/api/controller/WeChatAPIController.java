package com.yunsoo.api.controller;

import com.yunsoo.api.auth.service.OAuthAccountService;
import com.yunsoo.api.third.service.WeChatAPIService;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.third.dto.WeChatAccessToken;
import com.yunsoo.api.third.dto.WeChatPayRequest;
import com.yunsoo.api.third.dto.WeChatUser;
import com.yunsoo.api.third.dto.WeChatWebAccessToken;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by yan on 10/19/2016.
 */
@RestController
@RequestMapping("wechat")
public class WeChatAPIController {

    @Autowired
    private WeChatAPIService domain;

    @Autowired
    private OAuthAccountService oAuthAccountService;

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public WeChatAccessToken getWechatToken(){
        String orgId = domain.getOrgIdHasWeChatSettings(AuthUtils.fixOrgId(null));
        return domain.getUserAccessTokenByOrgId(orgId);
    }

    @RequestMapping(value = "web_user", method = RequestMethod.GET)
    public WeChatUser getWebUser(@RequestParam("code")String code, @RequestParam("details") Boolean detailsFlag){
        return domain.getWebUser(code, detailsFlag);
    }

    @RequestMapping(value = "web_token", method = RequestMethod.GET)
    public WeChatWebAccessToken getWebToken(@RequestParam("code")String code){
        return domain.getWebToken(code);
    }

    @RequestMapping(value = "config", method = RequestMethod.GET)
    public Map getWeChatConfig(@RequestParam("url")String url){

        return domain.getConfig(domain.getOrgIdHasWeChatSettings(AuthUtils.fixOrgId(null)), url);
    }

    @RequestMapping(value = "pay/{marketing_id}", method = RequestMethod.POST)
    public Map getWeChatPayConfig(@PathVariable(value = "marketing_id") String marketingId,
                                  @RequestBody WeChatPayRequest payRequest){
        String accountId = AuthUtils.fixAccountId(null);
        OAuthAccount authAccount = oAuthAccountService.getOAuthAccountByAccountId(accountId);
        if(authAccount == null)
            throw new NotFoundException("current account don't have oauth account");

        Map payConfig = domain.getPayConfig(domain.getOrgIdHasWeChatSettings(AuthUtils.fixOrgId(null)), authAccount.getoAuthOpenId(), marketingId, payRequest.getNonceString(),
                payRequest.getTimestamp(), payRequest.getNotifyUrl());
        return payConfig;
    }

}
