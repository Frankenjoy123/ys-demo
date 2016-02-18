package com.yunsoo.api.controller;

import com.yunsoo.api.domain.MarketingDomain;
import com.yunsoo.api.domain.ProductBaseDomain;
import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.dto.Marketing;
import com.yunsoo.api.dto.MktDrawRule;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.data.object.MktDrawRuleObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/marketing")
public class MarketingController {

    @Autowired
    private MarketingDomain marketingDomain;

    @Autowired
    private ProductDomain productDomain;

    @Autowired
    private ProductBaseDomain productBaseDomain;


    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "drawRule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRule createMktDrawRule(@RequestBody MktDrawRule mktDrawRule) {
        if (mktDrawRule == null) {
            throw new BadRequestException("marketing draw record can not be null");
        }
        String marketingId = mktDrawRule.getMarketingId();
        MarketingObject marketingObject = marketingDomain.getMarketingById(marketingId);
        if (marketingObject == null) {
            throw new NotFoundException("marketing can not be found by the id");
        }
        MktDrawRuleObject mktDrawRuleObject = mktDrawRule.toMktDrawRuleObject();
        String currentUserId = tokenAuthenticationService.getAuthentication().getDetails().getId();
        mktDrawRuleObject.setCreatedAccountId(currentUserId);
        mktDrawRuleObject.setCreatedDateTime(DateTime.now());

        MktDrawRuleObject newMktDrawRuleObject = marketingDomain.createMktDrawRule(mktDrawRuleObject);
        return new MktDrawRule(newMktDrawRuleObject);
    }

    //query marketing plan by org id
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Marketing> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                       Pageable pageable,
                                       HttpServletResponse response) {
        orgId = fixOrgId(orgId);
        Page<MarketingObject> marketingPage = marketingDomain.getMarketingByOrgId(orgId, pageable);
        if (pageable != null) {
            response.setHeader("Content-Range", marketingPage.toContentRange());
        }

        List<Marketing> marketingList = new ArrayList<>();
        marketingPage.getContent().forEach(object -> {
            Marketing marketing = new Marketing(object);
            ProductBaseObject pbo = productBaseDomain.getProductBaseById(object.getProductBaseId());
            if (pbo != null) {
                marketing.setProductBaseName(pbo.getName());
            }
            marketingList.add(marketing);
        });

        return marketingList;


//        List<Marketing> marketings = marketingPage.map(p -> {
//            Marketing marketing = new Marketing(p);
//            return marketing;
//        }).getContent();
//
//        return marketings;
    }

    //delete marketing plan by id
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMarketing(@PathVariable(value = "id") String id) {
        MarketingObject marketingObject = marketingDomain.getMarketingById(id);
        if (marketingObject != null) {
            marketingDomain.deleteMarketingById(id);
        }
    }

    private String fixOrgId(String orgId) {
        if (orgId == null || "current".equals(orgId)) {
            //current orgId
            return tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return orgId;
    }

}
