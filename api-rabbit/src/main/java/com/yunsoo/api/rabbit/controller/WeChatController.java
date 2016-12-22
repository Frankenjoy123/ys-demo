package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.third.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by yan on 12/19/2016.
 */
@RestController
@RequestMapping("wechat")
public class WeChatController {

    @Autowired
    private WeChatService weChatService;

    @RequestMapping(value = "config", method = RequestMethod.GET)
    public Map getWeChatConfig(@RequestParam("url")String url){
        return weChatService.getConfig(null, url);
    }

}
