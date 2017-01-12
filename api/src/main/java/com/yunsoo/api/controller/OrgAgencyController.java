package com.yunsoo.api.controller;

import com.yunsoo.api.auth.dto.Account;
import com.yunsoo.api.auth.dto.Organization;
import com.yunsoo.api.auth.service.AuthAccountService;
import com.yunsoo.api.auth.service.AuthOrganizationService;
import com.yunsoo.api.domain.OrgAgencyDomain;
import com.yunsoo.api.domain.OrganizationDomain;
import com.yunsoo.api.dto.Location;
import com.yunsoo.api.dto.OAuthAccount;
import com.yunsoo.api.dto.OrgAgency;
import com.yunsoo.api.dto.OrgAgencyDetails;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.api.util.PageUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.OrgAgencyObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by  : Haitao
 * Created on  : 2015/9/2
 * Descriptions:
 */

@RestController
@RequestMapping(value = "/organizationAgency")
public class OrgAgencyController {

    @Autowired
    private OrgAgencyDomain orgAgencyDomain;

    @Autowired
    private AuthAccountService authAccountService;

    @Autowired
    private AuthOrganizationService authOrganizationService;

    private static String SOURCE = "source";
    private static String SOURCE_TYPE = "source_type_code";

    //query by id
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PostAuthorize("hasPermission(returnObject, 'org_agency:read')")
    public OrgAgency getById(@PathVariable(value = "id") String id) {
        OrgAgencyObject orgAgencyObject = findOrgAgencyById(id);
        if (orgAgencyObject == null) {
            throw new NotFoundException("organization agency not found");
        }
        OrgAgency orgAgency = new OrgAgency(orgAgencyObject);
        return orgAgency;
    }

    @RequestMapping(value = "{id}/details", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'org_agency:read')")
    public OrgAgencyDetails getOrgAgencyDetails(@PathVariable(value = "id") String id) {
        String orgId = AuthUtils.fixOrgId(null);
        String accountId = AuthUtils.fixAccountId(null);
        String sourceId = id;
        Map<String, String> accountDetails = AuthUtils.getCurrentAccount().getDetails();

        Organization organization = authOrganizationService.getById(orgId);
        if(organization == null)
            throw new BadRequestException("current org not existing");

        if(id.equals("current"))
            sourceId = null;
        if (accountDetails != null && accountDetails.size() > 0)
            sourceId = accountDetails.get(SOURCE);

        OrgAgencyDetails details = new OrgAgencyDetails();
        details.setOrgName(organization.getName());
        List<OAuthAccount> accountList = orgAgencyDomain.getOAuthAccount(sourceId == null ? null : Arrays.asList(sourceId), accountId);
        if (accountList.size() > 0) {  //agency用户信息
            OAuthAccount account = accountList.get(0);
            details.setChildrenCount(orgAgencyDomain.count(account.getSource(), orgId));
            details.setAuthorizedChildrenCount(orgAgencyDomain.authorizedCount(orgId, account.getSource()));
            details.setOauthName(account.getName());
            details.setOauthGravatarUrl(account.getGravatarUrl());
            if (StringUtils.hasText(account.getSource())) {
                details.setAgencyId(account.getSource());
                details.setParentName(orgAgencyDomain.getParentOrgAgencyName(account.getSource()));
                if(!StringUtils.hasText(details.getParentName()))
                    details.setParentName(organization.getName());
            }
        }
        else {  //公司用户登陆获取信息
            Account currentAccount = authAccountService.getById(accountId);
            details.setChildrenCount(orgAgencyDomain.count(null, orgId));
            details.setAuthorizedChildrenCount(orgAgencyDomain.authorizedCount(orgId, null));
            details.setOauthName(currentAccount.getLastName()+ currentAccount.getFirstName());
            details.setOauthGravatarUrl("/organization/" + orgId + "/logo/image-128x128");
        }

        return details;

    }

    @RequestMapping(value = "{id}/list", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'org_agency:read')")
    public List<OrgAgency> list(@PathVariable(value = "id") String id, @RequestParam(value = "has_details", required = false) Boolean hasDetails) {
        String orgId = AuthUtils.fixOrgId(null);
        Map<String, String> details = AuthUtils.getCurrentAccount().getDetails();
        String sourceId = id;

        if(id.equals("current"))
            sourceId = null;

        if (details != null)
            sourceId = details.get(SOURCE);

        List<OrgAgency> agencyList = orgAgencyDomain.getListByOrgIdAndParentId(orgId, sourceId)
                .stream().map(OrgAgency::new).collect(Collectors.toList());
        if (hasDetails != null && hasDetails)
            orgAgencyDomain.getAgencyDetails(agencyList);

        return agencyList;
    }


    //query by org id
    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'org_agency:read')")
    public List<OrgAgency> getByFilter(@RequestParam(value = "org_id", required = false) String orgId,
                                       @RequestParam(value = "search_text", required = false) String searchText,
                                       @RequestParam(value = "parent_id", required = false) String parentId,
                                       @RequestParam(value = "start_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate startTime,
                                       @RequestParam(value = "end_datetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) org.joda.time.LocalDate endTime,
                                       Pageable pageable,
                                       HttpServletResponse response) {
        orgId = AuthUtils.fixOrgId(orgId);

        Page<OrgAgencyObject> orgAgencyPage = orgAgencyDomain.getOrgAgencyByOrgId(orgId, searchText, parentId, null, startTime, endTime, pageable);
        List<OrgAgency> agencyList = PageUtils.response(response, orgAgencyPage.map(OrgAgency::new), pageable != null);

        return agencyList;
    }

    //query locations
    @RequestMapping(value = "location", method = RequestMethod.GET)
    public List<Location> getLocationsByFilter(@RequestParam(value = "parent_id", required = false) String parentId) {
        return orgAgencyDomain.getLocationsByFilter(parentId).stream().map(Location::new).collect(Collectors.toList());
    }

    //create organization agency
    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#orgAgency.orgId, 'org', 'org_agency:create')")
    public OrgAgency create(@RequestBody OrgAgency orgAgency) {
        String currentAccountId = AuthUtils.getCurrentAccount().getId();
        OrgAgencyObject orgAgencyobject = orgAgency.toOrgAgencyObject();
        orgAgencyobject.setCreatedAccountId(currentAccountId);
        orgAgencyobject.setCreatedDateTime(DateTime.now());
        orgAgencyobject.setStatusCode(LookupCodes.OrgAgencyStatus.ACTIVATED);
        orgAgencyobject.setOrgId(AuthUtils.fixOrgId(orgAgency.getOrgId()));

        Map<String, String> details = AuthUtils.getCurrentAccount().getDetails();
        if(details != null) {
            String sourceId = details.get(SOURCE);
            String sourceType = details.get(SOURCE_TYPE);
            if ("agency".equals(sourceType))
                orgAgencyobject.setParentId(sourceId);
        }
        return new OrgAgency(orgAgencyDomain.createOrgAgency(orgAgencyobject));
    }

    //update organization agency
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public void update(@PathVariable("id") String id, @RequestBody OrgAgency orgAgency) {
        OrgAgencyObject orgAgencyObject = findOrgAgencyById(id);
        AuthUtils.checkPermission(orgAgencyObject.getOrgId(), "org_agency", "write");
        orgAgencyObject.setName(orgAgency.getName());
        orgAgencyObject.setLocationIds(orgAgency.getLocationIds());
        orgAgencyObject.setAddress(orgAgency.getAddress());
        orgAgencyObject.setDescription(orgAgency.getDescription());
        orgAgencyObject.setParentId(orgAgency.getParentId());
        orgAgencyObject.setAgencyResponsible(orgAgency.getAgencyResponsible());
        orgAgencyObject.setAgencyCode(orgAgency.getAgencyCode());
        orgAgencyObject.setAgencyPhone(orgAgency.getAgencyPhone());
        orgAgencyObject.setModifiedAccountId(AuthUtils.getCurrentAccount().getOrgId());
        orgAgencyObject.setModifiedDateTime(DateTime.now());
        orgAgencyDomain.updateOrgAgency(orgAgencyObject);
    }


    //delete organization agencys
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id) {
        OrgAgencyObject orgAgencyObject = orgAgencyDomain.getOrgAgencyById(id);
        if (orgAgencyObject != null) {
            if (LookupCodes.OrgAgencyStatus.DELETED.equals(orgAgencyObject.getStatusCode())) {
                throw new UnprocessableEntityException("illegal operation");
            }
            orgAgencyDomain.deleteOrgAgency(id);
        }
    }

    @RequestMapping(value = "{id}/enable", method = RequestMethod.PUT)
    public void activeAgency(@PathVariable("id") String agencyId) {
        OrgAgencyObject orgAgencyObject = orgAgencyDomain.getOrgAgencyById(agencyId);
        if (LookupCodes.OrgAgencyStatus.DEACTIVATED.equals(orgAgencyObject.getStatusCode())) {
            orgAgencyDomain.updateStatus(agencyId, LookupCodes.OrgAgencyStatus.ACTIVATED);
        }
    }

    @RequestMapping(value = "{id}/disable", method = RequestMethod.PUT)
    public void deactiveAgency(@PathVariable("id") String agencyId) {
        OrgAgencyObject orgAgencyObject = orgAgencyDomain.getOrgAgencyById(agencyId);
        if (LookupCodes.OrgAgencyStatus.ACTIVATED.equals(orgAgencyObject.getStatusCode())) {
            orgAgencyDomain.updateStatus(agencyId, LookupCodes.OrgAgencyStatus.DEACTIVATED);
        }
    }

    private OrgAgencyObject findOrgAgencyById(String id) {
        OrgAgencyObject orgAgencyObject = orgAgencyDomain.getOrgAgencyById(id);
        if (orgAgencyObject == null || LookupCodes.OrgAgencyStatus.DELETED.equals(orgAgencyObject.getStatusCode())) {
            throw new NotFoundException("organization agency not found");
        }
        return orgAgencyObject;
    }
}
