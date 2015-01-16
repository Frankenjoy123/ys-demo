package com.yunsoo.controller;

import com.yunsoo.ProductKey;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ProductKeyController {

    @RequestMapping(value = "/productkey", method = RequestMethod.GET)
    public ProductKey newProductKey() {
        return new ProductKey("EH6MhZATukqeKRADNOiBng", "12345");
    }
}
