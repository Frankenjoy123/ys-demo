package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.auth.service.OAuthAccountService;
import com.yunsoo.api.domain.WeChatAPIDomain;
import com.yunsoo.api.dto.OAuthAccount;
import com.yunsoo.api.dto.WeChatAccessToken;
import com.yunsoo.api.dto.WeChatUser;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yan on 10/19/2016.
 */
@RestController
@RequestMapping("wechat")
public class WechatController {

    @Autowired
    private WeChatAPIDomain domain;

    @Autowired
    private OAuthAccountService oAuthAccountService;

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public WeChatAccessToken getWechatToken(){
        return domain.getUserAccessTokenByAppId(null);
    }

    @RequestMapping(value = "web_user", method = RequestMethod.GET)
    public WeChatUser getWebUser(@RequestParam("code")String code, @RequestParam("details") Boolean detailsFlag){
        return domain.getWebUser(code, detailsFlag);
    }


    @RequestMapping(value = "config", method = RequestMethod.GET)
    public Map getWeChatConfig(@RequestParam("url")String url){
        return domain.getConfig(null, url);
    }

    @RequestMapping(value = "pay/{marketing_id}", method = RequestMethod.POST)
    public Map getWeChatPayConfig(@PathVariable(value = "marketing_id") String marketingId,
                                  @RequestParam("nonce_str") String nonceString,
                                  @RequestParam("timestamp") long timestamp){
        String accountId = AuthUtils.fixAccountId(null);
        OAuthAccount authAccount = oAuthAccountService.getOAuthAccountByAccountId(accountId);
        if(authAccount == null)
            throw new NotFoundException("current account don't have oauth account");

        return domain.getPayConfig(null, authAccount.getoAuthOpenId(), marketingId, nonceString, timestamp);
    }
}
