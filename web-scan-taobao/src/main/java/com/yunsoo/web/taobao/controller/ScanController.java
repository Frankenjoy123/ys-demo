package com.yunsoo.web.taobao.controller;

import com.yunsoo.web.taobao.model.ProductModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by:   Lijian
 * Created on:   2016-01-21
 * Descriptions:
 */
@Controller
@RequestMapping("scan")
public class ScanController {

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public String scan(@PathVariable("key") String key, HttpServletRequest request, Model model) {
        //todo

        ProductModel productModel = new ProductModel();
        productModel.setProductKey(key);
        model.addAttribute("productKey", key);
        return "default/main";
    }

    @RequestMapping(value = "{key}/scanned", method = RequestMethod.GET)
    public String getScanResult(@PathVariable("key") String key, Model model) {
        //todo

        return "default/main";
    }

}
