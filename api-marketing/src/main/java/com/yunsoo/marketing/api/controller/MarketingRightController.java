package com.yunsoo.marketing.api.controller;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.marketing.dto.MarketingRight;
import com.yunsoo.marketing.service.MarketingRightService;
import com.yunsoo.marketing.service.MarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
@RestController
@RequestMapping("/marketing/{marketing_id}/right")
public class MarketingRightController {

    @Autowired
    private MarketingService marketingService;

    @Autowired
    private MarketingRightService marketingRightService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<MarketingRight> getMarketingRights(@PathVariable("marketing_id") String marketingId) {
        return marketingRightService.getByMarketingId(marketingId);
    }


    private MarketingRight findMarketingRightById(String marketingRightId) {
        MarketingRight marketingRight = marketingRightService.getById(marketingRightId);
        if (marketingRight == null) {
            throw new NotFoundException("marketing right not found");
        }
        return marketingRight;
    }

}
