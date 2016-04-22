package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductKeyOrderDomain;
import com.yunsoo.api.dto.ProductKeyCredit;
import com.yunsoo.api.util.AuthUtils;
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


    @RequestMapping(value = "")
    //@PreAuthorize("hasPermission(#orgId, 'org', 'product_key_credit:read')")
    public List<ProductKeyCredit> calculate(@RequestParam(value = "org_id", required = false) String orgId,
                                            @RequestParam(value = "product_base_id", required = false) String productBaseId) {
        orgId = AuthUtils.fixOrgId(orgId);
        return productKeyOrderDomain.getProductKeyCredits(orgId, productBaseId);
    }

}
