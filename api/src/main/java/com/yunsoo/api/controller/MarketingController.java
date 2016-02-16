package com.yunsoo.api.controller;

import com.yunsoo.api.domain.MarketingDomain;
import com.yunsoo.api.domain.ProductDomain;
import com.yunsoo.api.dto.MktDrawRule;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.MarketingObject;
import com.yunsoo.common.data.object.MktDrawRuleObject;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "drawRule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MktDrawRule createMktDrawRecord(@RequestBody MktDrawRule mktDrawRule) {
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
}
