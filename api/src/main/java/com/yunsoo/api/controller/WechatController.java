package com.yunsoo.api.controller;

import com.yunsoo.api.Constants;
import com.yunsoo.api.domain.WeChatAPIDomain;
import com.yunsoo.api.dto.WeChatAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yan on 10/19/2016.
 */
@RestController
@RequestMapping("wechat")
public class WechatController {

    @Autowired
    WeChatAPIDomain domain;

    @RequestMapping(value = "token", method = RequestMethod.GET)
    public WeChatAccessToken getWechatToken(){
        return new WeChatAccessToken(domain.getUserAccessTokenByOrgId(Constants.Ids.YUNSU_ORGID));
    }
}
