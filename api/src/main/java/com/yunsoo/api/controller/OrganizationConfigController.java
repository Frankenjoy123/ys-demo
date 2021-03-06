package com.yunsoo.api.controller;

import com.yunsoo.api.domain.DomainDirectoryDomain;
import com.yunsoo.api.domain.OrganizationConfigDomain;
import com.yunsoo.api.third.service.WeChatAPIService;
import com.yunsoo.api.third.dto.WeChatOpenIdList;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.DomainDirectoryObject;
import com.yunsoo.common.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2016-05-25
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationConfigController {

    @Autowired
    private OrganizationConfigDomain organizationConfigDomain;

    @Autowired
    private DomainDirectoryDomain domainDirectoryDomain;

    @Autowired
    private WeChatAPIService weChatAPIDomain;


    @RequestMapping(value = "public/config", method = RequestMethod.GET)
    public Map<String, Object> getPublicConfig(@RequestParam(value = "org_id", required = false) String orgId,
                                               @RequestParam(value = "domain", required = false) String domain,
                                               @RequestParam(value = "prefix", required = false) String prefix) {
        if (orgId == null && domain != null) {
            DomainDirectoryObject domainDirectory = domainDirectoryDomain.search(domainDirectoryDomain.getDomainDirectoryObjectMap(), domain);
            if (domainDirectory != null) orgId = domainDirectory.getOrgId();
        }
        return organizationConfigDomain.getConfig(orgId, true, prefix);
    }

    @RequestMapping(value = "{id}/config", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization_config:read')")
    public Map<String, Object> getConfigById(@PathVariable(value = "id") String orgId,
                                             @RequestParam(value = "prefix", required = false) String prefix) {
        orgId = AuthUtils.fixOrgId(orgId);
        return organizationConfigDomain.getConfig(orgId, false, prefix);
    }

    @RequestMapping(value = "{id}/config", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization_config:write')")
    public void putConfig(@PathVariable(value = "id") String orgId,
                          @RequestBody Map<String, Object> configItems) {
        orgId = AuthUtils.fixOrgId(orgId);
        organizationConfigDomain.saveConfig(orgId, configItems);
    }

    @RequestMapping(value = "{id}/config/wechat", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization_config:write')")
    public void putWechatConfig(@PathVariable(value = "id") String orgId,
                          @RequestBody Map<String, Object> configItems) {
        orgId = AuthUtils.fixOrgId(orgId);
        String secret = configItems.get("webchat.app_secret").toString();
        String appId = configItems.get("webchat.app_id").toString();

        WeChatOpenIdList list = weChatAPIDomain.getOpenIds(appId, secret);
        if(list!=null && list.getTotal() > 0 )
            organizationConfigDomain.saveConfig(orgId, configItems);
        else
            throw new BadRequestException("app id or secret not correct");

    }

}
