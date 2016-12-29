package com.yunsoo.api.controller;

import com.yunsoo.api.auth.service.OAuthAccountService;
import com.yunsoo.api.third.dto.*;
import com.yunsoo.api.third.service.WeChatAPIService;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Created by yan on 10/19/2016.
 */
@RestController
@RequestMapping("wechat")
public class WeChatAPIController {

    @Autowired
    private WeChatAPIService weChatAPIService;

    @Autowired
    private OAuthAccountService oAuthAccountService;

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public WeChatAccessToken getWechatToken(){
        String orgId = weChatAPIService.getOrgIdHasWeChatSettings(AuthUtils.fixOrgId(null));
        return weChatAPIService.getUserAccessTokenByOrgId(orgId);
    }

    @RequestMapping(value = "web_user", method = RequestMethod.GET)
    public WeChatUser getWebUser(@RequestParam("code")String code, @RequestParam("details") Boolean detailsFlag){
        return weChatAPIService.getWebUser(code, detailsFlag);
    }

    @RequestMapping(value = "web_token", method = RequestMethod.GET)
    public WeChatWebAccessToken getWebToken(@RequestParam("code")String code){
        return weChatAPIService.getWebToken(code);
    }

    @RequestMapping(value = "config", method = RequestMethod.GET)
    public Map getWeChatConfig(@RequestParam("url")String url){

        return weChatAPIService.getConfig(weChatAPIService.getOrgIdHasWeChatSettings(AuthUtils.fixOrgId(null)), url);
    }

    @RequestMapping(value = "pay/{marketing_id}", method = RequestMethod.POST)
    public Map getWeChatPayConfig(@PathVariable(value = "marketing_id") String marketingId,
                                  @RequestBody WeChatPayRequest payRequest){
        String accountId = AuthUtils.fixAccountId(null);
        OAuthAccount authAccount = oAuthAccountService.getOAuthAccountByAccountId(accountId);
        if(authAccount == null)
            throw new NotFoundException("current account don't have oauth account");

        Map payConfig = weChatAPIService.getPayConfig(weChatAPIService.getOrgIdHasWeChatSettings(AuthUtils.fixOrgId(null)), authAccount.getoAuthOpenId(), marketingId, payRequest.getNonceString(),
                payRequest.getTimestamp(), payRequest.getNotifyUrl());
        return payConfig;
    }

    @RequestMapping(value = "server_config", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveWeChatServerConfig(@RequestParam("file") MultipartFile keyFile,
                                       @RequestParam("app_id") String appId, @RequestParam("app_secret") String appSecret,
                                       @RequestParam("private_key") String privateKey, @RequestParam("mch_id") String mchId){
        try {
            WeChatServerConfig config = new WeChatServerConfig();
            config.setOrgId(AuthUtils.fixOrgId(null));
            config.setAppId(appId);
            config.setAppSecret(appSecret);
            config.setPrivateKey(privateKey);
            config.setMchId(mchId);
            config.setSslKey(keyFile.getBytes());
            weChatAPIService.saveWeChatServerConfig(config);

        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("save wechat config failed");
        }

    }


    @RequestMapping(value = "server_config", method = RequestMethod.GET)
    public WeChatServerConfig getWeChatServerConfig(){
        WeChatServerConfig config = weChatAPIService.getServerConfig(AuthUtils.fixOrgId(null));
        if(config == null)
            throw new NotFoundException("current org don't have wechat config");
        return config;
    }
}
