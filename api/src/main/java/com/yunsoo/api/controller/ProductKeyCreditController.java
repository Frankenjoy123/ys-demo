package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductKeyOrderDomain;
import com.yunsoo.api.dto.ProductKeyCredit;
import com.yunsoo.api.dto.ProductKeyOrder;
import com.yunsoo.api.security.TokenAuthenticationService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/7
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productKeyCredit")
public class ProductKeyCreditController {

    @Autowired
    private ProductKeyOrderDomain productKeyOrderDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "")
    public List<ProductKeyCredit> calculate(@RequestParam(value = "org_id", required = false) String orgId,
                                            @RequestParam(value = "product_base_id", required = false) String productBaseId) {
        if (orgId == null) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return getProductKeyCredits(orgId, productBaseId);
    }


    private List<ProductKeyCredit> getProductKeyCredits(String orgId, String productBaseId) {
        List<ProductKeyCredit> credits = new ArrayList<>();
        Map<String, ProductKeyCredit> creditMap = new HashMap<>();
        // search orders by [active = true and expire_datetime >= now], including remain is 0
        List<ProductKeyOrder> orders = productKeyOrderDomain.getOrdersByFilter(orgId, true, null, DateTime.now(), productBaseId, null, null);
        orders.forEach(o -> {
            if (o != null) {
                String pId = o.getProductBaseId();
                ProductKeyCredit c;
                if (creditMap.containsKey(pId)) {
                    c = creditMap.get(pId);
                } else {
                    c = new ProductKeyCredit(pId);
                    creditMap.put(pId, c);
                    credits.add(c);
                }
                c.setTotalCredit(c.getTotalCredit() + o.getTotal());
                c.setRemainCredit(c.getRemainCredit() + o.getRemain());
            }
        });

        return credits;
    }
}
