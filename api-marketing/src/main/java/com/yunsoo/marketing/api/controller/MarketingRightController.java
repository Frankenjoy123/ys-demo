package com.yunsoo.marketing.api.controller;

import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.marketing.dto.MarketingRight;
import com.yunsoo.marketing.service.MarketingRightService;
import com.yunsoo.marketing.service.MarketingService;
import com.yunsoo.marketing.service.UserRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    private UserRightService userRightService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<MarketingRight> getMarketingRights(@PathVariable("marketing_id") String marketingId) {
        return marketingRightService.getByMarketingId(marketingId);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#request, 'marketingRight:create')")
    public MarketingRight create(@RequestBody @Valid MarketingRight marketingRight) {
        return marketingRightService.createMarketingRight(marketingRight);
    }

    @RequestMapping(value = "list", method = RequestMethod.PATCH)
    public void createMarketingRights(@PathVariable("marketing_id") String marketingId, @RequestBody List<MarketingRight> marketingRightList) {
        marketingRightService.putMarketingRightsByMarketingId(marketingId, marketingRightList);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdateMarketing(@PathVariable("marketing_id") String marketingId, @PathVariable("id") String id, @RequestBody MarketingRight marketingRight) {
        marketingRight.setId(id);
        marketingRight.setMarketingId(marketingId);
        marketingRightService.patchUpdate(marketingRight);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        marketingRightService.deleteMarketingRight(id);
    }

    @RequestMapping(value = "/sum", method = RequestMethod.GET)
    public Long countDrawPrizeByDrawRuleId(
            @RequestParam(value = "marketing_id") String marketingId,
            @RequestParam(value = "marketing_right_id", required = false) String marketingRightId) {
        if (StringUtils.isEmpty(marketingId)) {
            throw new BadRequestException("marketing id is not valid");
        }
        if (StringUtils.isEmpty(marketingRightId))
            marketingRightId = null;

        Long quantity = userRightService.sumUserRight(marketingId, marketingRightId);
        return quantity;
    }

    private MarketingRight findMarketingRightById(String marketingRightId) {
        MarketingRight marketingRight = marketingRightService.getById(marketingRightId);
        if (marketingRight == null) {
            throw new NotFoundException("marketing right not found");
        }
        return marketingRight;
    }

}
