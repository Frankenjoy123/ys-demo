package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.dto.User;
import com.yunsoo.api.rabbit.key.dto.Product;
import com.yunsoo.api.rabbit.key.service.ProductService;
import com.yunsoo.api.rabbit.third.dto.WeChatServerConfig;
import com.yunsoo.api.rabbit.third.dto.WeChatUser;
import com.yunsoo.api.rabbit.third.service.WeChatService;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by yan on 12/19/2016.
 */
@RestController
@RequestMapping("wechat")
public class WeChatController {

    Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private UserDomain userDomain;

    @RequestMapping(value = "config", method = RequestMethod.GET)
    public Map getWeChatConfig(@RequestParam("url") String url) {
        return weChatService.getConfig(null, url);
    }

    @RequestMapping(value = "qrcode/{key}", method = RequestMethod.POST)
    public String getQRCodeTicket(@PathVariable("key") String key) {
        WeChatServerConfig config = getWeChatServerConfig(key);
        return weChatService.createQRCode(key, config.getOrgId());
    }

    @RequestMapping(value = "config/{key}", method = RequestMethod.GET)
    public WeChatServerConfig getWeChatServerConfig(@PathVariable("key") String key) {
        Product product = productService.getProductByKey(key);
        if (product == null) {
            throw new NotFoundException("key not found");
        }

        ProductKeyBatchObject keyBatchObject = productDomain.getProductKeyBatch(product.getKeyBatchId());
        if (keyBatchObject == null) {
            throw new NotFoundException("key related key batch not found");
        }

        WeChatServerConfig config = weChatService.getOrgIdHasWeChatSettings(keyBatchObject.getOrgId());
        if (config == null)
            throw new NotFoundException("wechat settings not found with org: " + keyBatchObject.getOrgId());

        return config;
    }

    @RequestMapping(value = "web_user/{key}", method = RequestMethod.GET)
    public User getWebUser(@PathVariable("key") String key,
                           @RequestParam("code") String code,
                           @RequestParam("details") Boolean detailsFlag) {
        WeChatServerConfig config = getWeChatServerConfig(key);

        WeChatUser user = weChatService.getWebUser(code, detailsFlag, config.getOrgId());

        UserObject userObject = new UserObject();
        userObject.setName(user.getNickName());
        //in wechat, 2 is female, 1 is male
        userObject.setSex(user.getSex() != null ? (user.getSex().equals("2") ? true : (user.getSex().equals(1) ? false : null)) : null);
        userObject.setGravatarUrl(user.getImageUrl());
        userObject.setCity(user.getCity());
        userObject.setProvince(user.getProvince());
        userObject.setOauthOpenid(user.getOpenId());
        userObject.setOauthTypeCode(LookupCodes.UserOAuthType.WECHAT);
        userObject.setCreatedDateTime(DateTime.now());

        UserObject existingUser = userDomain.getUserByOpenIdAndType(user.getOpenId(), LookupCodes.UserOAuthType.WECHAT);
        if (existingUser == null) {
            existingUser = userDomain.createUser(userObject);
        } else if(user.getNickName() != null){
            userObject.setId(existingUser.getId());
            userDomain.patchUpdateUser(userObject);
        }
        return new User(existingUser);
    }

}
