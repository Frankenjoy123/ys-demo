package com.yunsoo.api.controller;

import com.yunsoo.api.domain.DomainDirectoryDomain;
import com.yunsoo.api.domain.OrganizationConfigDomain;
import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.dto.DomainDirectory;
import com.yunsoo.api.dto.Organization;
import com.yunsoo.api.dto.OrganizationConfig;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.object.DomainDirectoryObject;
import com.yunsoo.common.data.object.OrganizationConfigObject;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-04-13
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private OrganizationDomain organizationDomain;

    @Autowired
    private OrganizationConfigDomain organizationConfigDomain;

    @Autowired
    private DomainDirectoryDomain domainDirectoryDomain;

    //region organization

    @RequestMapping(value = "organization/public", method = RequestMethod.GET)
    public OrganizationConfig getPublicConfigByDomain(@RequestParam("domain") String domain) {
        DomainDirectoryObject domainDirectory = domainDirectoryDomain.search(domainDirectoryDomain.getDomainDirectoryObjectMap(), domain);
        OrganizationObject org = findOrg(domainDirectory.getOrgId());
        OrganizationConfigObject configObject = organizationConfigDomain.getOrgConfigByOrgId(org.getId());
        if (configObject == null) {
            throw new NotFoundException("organization config not found");
        }
        //public config items
        OrganizationConfig config = new OrganizationConfig();
        config.setOrganization(new Organization(org));
        if (configObject.getEnterprise() != null) {
            OrganizationConfig.Enterprise enterprise = new OrganizationConfig.Enterprise();
            enterprise.setName(configObject.getEnterprise().getName());
            if (configObject.getEnterprise().getLogin() != null) {
                OrganizationConfig.Enterprise.Login login = new OrganizationConfig.Enterprise.Login();
                login.setBackgroundImage(configObject.getEnterprise().getLogin().getBackgroundImage());
                if (configObject.getEnterprise().getLogin().getBandApplicationLink() != null) {
                    OrganizationConfig.Enterprise.Login.BandApplicationLink bandApplicationLink = new OrganizationConfig.Enterprise.Login.BandApplicationLink();
                    bandApplicationLink.setName(configObject.getEnterprise().getLogin().getBandApplicationLink().getName());
                    bandApplicationLink.setUrl(configObject.getEnterprise().getLogin().getBandApplicationLink().getUrl());
                    login.setBandApplicationLink(bandApplicationLink);
                }
                enterprise.setLogin(login);
            }
            config.setEnterprise(enterprise);
        }
        return config;
    }

    @RequestMapping(value = "organization", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization_config:read')")
    public OrganizationConfig getConfig(@RequestParam(value = "org_id", required = false) String orgId) {
        orgId = AuthUtils.fixOrgId(orgId);
        OrganizationObject org = findOrg(orgId);
        OrganizationConfigObject configObject = organizationConfigDomain.getOrgConfigByOrgId(orgId);
        if (configObject == null) {
            throw new NotFoundException("organization config not found");
        }
        OrganizationConfig config = new OrganizationConfig(configObject);
        config.setOrganization(new Organization(org));
        return config;
    }

    @RequestMapping(value = "organization", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#orgId, 'org', 'organization_config:write')")
    public void putConfig(@RequestParam(value = "org_id", required = false) String orgId,
                          @RequestBody OrganizationConfig config) {
        orgId = AuthUtils.fixOrgId(orgId);
        organizationConfigDomain.saveOrgConfig(orgId, config.toOrganizationConfigObject());
    }

    //endregion

    //region domain directory

    @RequestMapping(value = "domainDirectory", method = RequestMethod.GET)
    @PreAuthorize("hasPermission('*', 'org', 'domain_directory_config:read')")
    public List<DomainDirectory> getAll() {
        return domainDirectoryDomain.getDomainDirectoryObjectList().stream()
                .map(DomainDirectory::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "domainDirectory", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission('*', 'org', 'domain_directory_config:write')")
    public void putOne(@RequestParam(name = "name", required = true) String name,
                       @RequestBody DomainDirectory domainDirectory) {
        DomainDirectoryObject domainDirectoryObject = domainDirectory.toDomainDirectoryObject();
        domainDirectoryObject.setName(name);
        domainDirectoryDomain.putDomainDirectoryObject(domainDirectoryObject);
    }

    @RequestMapping(value = "domainDirectory", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission('*', 'org', 'domain_directory_config:delete')")
    public void deleteOne(@RequestParam(name = "name", required = true) String name) {
        if (StringUtils.hasText(name)) {
            domainDirectoryDomain.deleteDomainDirectoryObjectByName(name);
        }
    }

    //endregion

    public OrganizationObject findOrg(String orgId) {
        OrganizationObject org = organizationDomain.getOrganizationById(orgId);
        if (org == null) {
            throw new NotFoundException("");
        }
        return org;
    }

}
