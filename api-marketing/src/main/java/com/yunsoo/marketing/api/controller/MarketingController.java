package com.yunsoo.marketing.api.controller;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.marketing.dto.Marketing;
import com.yunsoo.marketing.service.MarketingRightService;
import com.yunsoo.marketing.service.MarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@RestController
@RequestMapping("/marketing")
public class MarketingController {

    @Autowired
    private MarketingService marketingService;

    @Autowired
    private MarketingRightService marketingRightService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Marketing getMarketingById(@PathVariable("id") String marketingId) {
        return findMarketingById(marketingId);
    }

    private Marketing findMarketingById(String marketingId) {
        Marketing marketing = marketingService.getMarketingById(marketingId);
        if (marketing == null) {
            throw new NotFoundException("marketing not found");
        }
        return marketing;
    }

}
