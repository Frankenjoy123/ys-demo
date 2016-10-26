package com.yunsoo.marketing.api.controller;

import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.marketing.api.util.AuthUtils;
import com.yunsoo.marketing.api.util.PageUtils;
import com.yunsoo.marketing.dto.Marketing;
import com.yunsoo.marketing.service.MarketingRightService;
import com.yunsoo.marketing.service.MarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

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

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#request, 'marketing:create')")
    public Marketing create(@RequestBody @Valid Marketing marketing) {
        return marketingService.createMarketing(marketing);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PATCH)
    public void patchUpdateMarketing(@PathVariable("id") String id, @RequestBody Marketing marketing) {
        marketing.setId(id);
        marketingService.patchUpdate(marketing);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'marketing:read')")
    public List<Marketing> getByOrgId(@RequestParam(value = "org_id", required = false) String orgId,
                                      Pageable pageable,
                                      HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId);

        Page<Marketing> page = marketingService.getByOrgId(orgId, pageable);
        return PageUtils.response(response, page, pageable != null);
    }

    private Marketing findMarketingById(String marketingId) {
        Marketing marketing = marketingService.getMarketingById(marketingId);
        if (marketing == null) {
            throw new NotFoundException("marketing not found");
        }
        return marketing;
    }

}
