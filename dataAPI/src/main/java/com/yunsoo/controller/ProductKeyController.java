package com.yunsoo.controller;

import com.yunsoo.ProductKey;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/productkey")
public class ProductKeyController {

    @RequestMapping(value = "/newkey", method = RequestMethod.GET)
    public ProductKey newProductKey() {
        return new ProductKey("EH6MhZATukqeKRADNOiBng", "12345");
    }

    //Response for Scanning key .
    @RequestMapping(value = "/scan", method = RequestMethod.GET)
    public String getScanInfor(@PathVariable(value = "key") String key) {
        //to-do
        return "{  result: 真品" +
                " companyIcon: 'http://abc.com' " +
                " productIcon: 'http://abc.com/8' " +
                " Scan: 杭州西湖区X用户扫描可口可乐，  2015年1月28号 " +
                " logistic: 杭州富阳仓库，  2015年1月27号 " +
                " product: 可口可乐，水，食品添加等等。" +
                "}";
    }

    //batch request product keys
    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public void requestKeys(@PathVariable(value = "number") Integer number) {
        //to-do
    }




}
