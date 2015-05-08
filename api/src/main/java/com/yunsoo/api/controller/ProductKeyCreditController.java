package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductKeyOrderDomain;
import com.yunsoo.api.dto.ProductKeyCredit;
import com.yunsoo.api.security.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return productKeyOrderDomain.getProductKeyCredits(orgId, productBaseId);
    }

}
