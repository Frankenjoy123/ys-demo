package com.yunsoo.api.rabbit.controller;

import com.yunsoo.api.rabbit.domain.ProductDomain;
import com.yunsoo.api.rabbit.key.dto.Product;
import com.yunsoo.api.rabbit.key.service.ProductService;
import com.yunsoo.api.rabbit.third.service.WeChatService;
import com.yunsoo.common.web.exception.NotFoundException;
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

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "config", method = RequestMethod.GET)
    public Map getWeChatConfig(@RequestParam("url")String url){
        return weChatService.getConfig(null, url);
    }

    @RequestMapping(value = "qrcode/{key}", method = RequestMethod.POST)
    public String getQRCodeTicket(@PathVariable("key")String key){
        Product product = productService.getProductByKey(key);
        if (product == null) {
            throw new NotFoundException("key not found");
        }
        return weChatService.createQRCode(key);
    }

}
