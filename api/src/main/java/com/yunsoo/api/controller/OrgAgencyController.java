package com.yunsoo.api.controller;

import com.yunsoo.api.domain.OrgAgencyDomain;
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

    @RequestMapping(value = "details", method= RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'org_agency:read')")
    public OrgAgencyDetails getOrgAgencyDetails(){
        String orgId = AuthUtils.fixOrgId(null);
        Map<String, String> accountDetails = AuthUtils.getCurrentAccount().getDetails();
        String sourceId = accountDetails.get(SOURCE);
        OrgAgencyDetails details = new OrgAgencyDetails();
        List<OAuthAccount> accountList = orgAgencyDomain.getOAuthAccount(Arrays.asList(sourceId));
        if(accountList.size() == 0)
            throw new NotFoundException("no agency id");

        OAuthAccount account = accountList.get(0);

        if(LookupCodes.TraceSourceType.AGENCY.equals(account.getSourceTypeCode())){
            details.setChildrenCount(orgAgencyDomain.count(account.getSource()));
            details.setAuthorizedChildrenCount(orgAgencyDomain.authorizedCount(orgId, account.getSource()));
            details.setOauthName(account.getName());
            details.setOauthGravatarUrl(account.getGravatarUrl());
            details.setAgencyId(account.getSource());
            details.setParentName(orgAgencyDomain.getParentOrgAgencyName(account.getSource()));

            return details;

        }
        return  null;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#orgId, 'org', 'org_agency:read')")
    public List<OrgAgency> list(@RequestParam(value = "has_details", required = false) Boolean hasDetails,
                                @PageableDefault(size = 1000, sort = {"name"}) Pageable pageable,
                                 HttpServletResponse response) {
        String orgId = AuthUtils.fixOrgId(null);
        Map<String, String> details = AuthUtils.getCurrentAccount().getDetails();
        String sourceId = details.get(SOURCE);  String sourceType = details.get(SOURCE_TYPE);
        String parentId="";
        if("agency".equals(sourceType)) {
            parentId = sourceId;
        }

        Page<OrgAgencyObject> orgAgencyPage = orgAgencyDomain.getOrgAgencyByOrgId(orgId, null, parentId, null, null, null, pageable);

        List<OrgAgency> agencyList = PageUtils.response(response, orgAgencyPage.map(OrgAgency::new), pageable != null);
        if(hasDetails!=null && hasDetails)
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
        String sourceId = details.get(SOURCE);
        String sourceType = details.get(SOURCE_TYPE);
        if("agency".equals(sourceType))
            orgAgencyobject.setParentId(sourceId);

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
