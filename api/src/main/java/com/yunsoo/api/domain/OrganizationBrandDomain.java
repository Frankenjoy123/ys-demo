package com.yunsoo.api.domain;

import com.yunsoo.api.Constants;
import com.yunsoo.api.auth.dto.Account;
import com.yunsoo.api.auth.dto.AccountCreationRequest;
import com.yunsoo.api.auth.dto.Organization;
import com.yunsoo.api.auth.service.AuthAccountService;
import com.yunsoo.api.auth.service.AuthOrganizationService;
import com.yunsoo.api.auth.service.AuthPermissionService;
import com.yunsoo.api.client.DataApiClient;
import com.yunsoo.api.dto.OrganizationBrand;
import com.yunsoo.common.data.object.OrgBrandObject;
import com.yunsoo.common.util.ObjectIdGenerator;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-08-05
 * Descriptions:
 */
@Component
public class OrganizationBrandDomain {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    private DataApiClient dataApiClient;

    @Autowired
    private AuthAccountService authAccountService;

    @Autowired
    private AuthPermissionService authPermissionService;

    @Autowired
    private AuthOrganizationService authOrganizationService;


    public OrgBrandObject getOrgBrandObjectById(String orgId) {
        try {
            return dataApiClient.get("orgBrand/{id}", OrgBrandObject.class, orgId);
        } catch (NotFoundException ex) {
            log.warn(ex.getMessage(), ex);
            return null;
        }
    }

    public Page<OrgBrandObject> getOrgBrandObjectPaged(String carrierId,
                                                       List<String> orgIdIn,
                                                       String orgName,
                                                       String categoryId,
                                                       DateTime createdDateTimeGE,
                                                       DateTime createdDateTimeLE,
                                                       String searchText,
                                                       Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("carrier_id", carrierId)
                .append("org_id_in", orgIdIn)
                .append("org_name", orgName)
                .append("category_id", categoryId)
                .append("created_datetime_ge", createdDateTimeGE)
                .append("created_datetime_le", createdDateTimeLE)
                .append("search_text", searchText)
                .append(pageable)
                .build();
        return dataApiClient.getPaged("orgBrand" + query, new ParameterizedTypeReference<List<OrgBrandObject>>() {
        });
    }

    public Long countOrgBrandByCarrierId(String carrierId) {
        if (StringUtils.isEmpty(carrierId)) {
            return 0L;
        }
        return dataApiClient.get("orgBrand/count?carrier_id={0}", Long.class, carrierId);
    }

    public List<String> getBrandIdsByCarrierId(String carrierId) {
        if (StringUtils.isEmpty(carrierId)) {
            return new ArrayList<>();
        }
        return dataApiClient.get("orgBrand/orgId?carrier_id={0}", new ParameterizedTypeReference<List<String>>() {
        }, carrierId);
    }

    public OrganizationBrand getOrganizationBrandById(String orgId) {
        Organization organization = authOrganizationService.getById(orgId);
        if (organization == null) {
            return null;
        }
        OrgBrandObject orgBrandObject = getOrgBrandObjectById(orgId);
        if (orgBrandObject == null) {
            return null;
        }
        return new OrganizationBrand(organization, orgBrandObject);
    }

    public Page<OrganizationBrand> getOrganizationBrandPaged(String carrierId,
                                                             String statusCode,
                                                             String orgName,
                                                             String categoryId,
                                                             DateTime createdDateTimeGE,
                                                             DateTime createdDateTimeLE,
                                                             String searchText,
                                                             Pageable pageable) {
        Integer page, total, count;
        List<OrgBrandObject> list = getOrgBrandObjectPaged(carrierId, null, orgName, categoryId, createdDateTimeGE, createdDateTimeLE, searchText, pageable).getContent();
        List<String> ids = list.stream().map(OrgBrandObject::getOrgId).collect(Collectors.toList());
        Map<String, Organization> organizationMap = authOrganizationService.getByIdsIn(ids)
                .stream().collect(Collectors.toMap(Organization::getId, o -> o));
        if (statusCode != null) {
            list = list.stream().filter(o -> {
                Organization org = organizationMap.get(o.getOrgId());
                return org != null && statusCode.equals(org.getStatusCode());
            }).collect(Collectors.toList());
        }
        if (pageable != null) {
            int size = pageable.getPageSize();
            page = pageable.getPageNumber();
            count = list.size();
            total = size == 0 ? 1 : (int) Math.ceil((double) count / (double) size);
            list = list.stream().skip(page * size).limit(size).collect(Collectors.toList());
        } else {
            page = 0;
            total = 1;
            count = list.size();
        }
        return new Page<>(list, page, total, count).map(o -> new OrganizationBrand(organizationMap.get(o.getOrgId()), o));
    }

    public OrganizationBrand createOrganizationBrand(OrganizationBrand organizationBrand) {
        String orgId = ObjectIdGenerator.getNew();

        authPermissionService.addOrgIdToDefaultRegion(orgId);
        OrganizationBrand returnObj = createOrganizationBrand(
                orgId,
                organizationBrand.getName(),
                organizationBrand.getDescription(),
                organizationBrand.toOrgBrandObject());

        AccountCreationRequest accountCreationRequest = new AccountCreationRequest();
        accountCreationRequest.setEmail(returnObj.getEmail());
        accountCreationRequest.setIdentifier("admin");
        accountCreationRequest.setFirstName(returnObj.getContactName());
        accountCreationRequest.setLastName("");
        accountCreationRequest.setPassword("admin");
        accountCreationRequest.setPhone(returnObj.getContactMobile());
        accountCreationRequest.setOrgId(returnObj.getId());
        accountCreationRequest.setCreatedAccountId(Constants.Ids.SYSTEM_ACCOUNT_ID);

        Account account = authAccountService.create(accountCreationRequest);
        authPermissionService.allocateAdminPermissionOnCurrentOrgToAccount(account.getId());
        return returnObj;
    }

    public OrganizationBrand createOrganizationBrand(String orgId, String name, String description, OrgBrandObject orgBrandObject) {
        Organization organization = authOrganizationService.createOrganizationForBrand(orgId, name, description);
        orgBrandObject.setOrgId(organization.getId());
        orgBrandObject.setOrgName(organization.getName());
        orgBrandObject.setCreatedDateTime(organization.getCreatedDateTime());
        try {
            dataApiClient.put("orgBrand/{id}", orgBrandObject, orgBrandObject.getOrgId());
        } catch (Exception ex) {
            authOrganizationService.deleteOrganization(organization.getId());
        }
        authOrganizationService.enableOrganization(organization.getId());
        return new OrganizationBrand(organization, orgBrandObject);
    }

    public void patchUpdateOrganizationBrand(OrganizationBrand orgBrand) {
        if (orgBrand == null || StringUtils.isEmpty(orgBrand.getId())) {
            throw new BadRequestException();
        }
        OrgBrandObject obj = getOrgBrandObjectById(orgBrand.getId());
        if (obj == null) {
            throw new NotFoundException("organization brand not found by id: " + orgBrand.getId());
        }
        if (orgBrand.getContactName() != null) obj.setContactName(orgBrand.getContactName());
        if (orgBrand.getContactMobile() != null) obj.setContactMobile(orgBrand.getContactMobile());
        if (orgBrand.getEmail() != null) obj.setEmail(orgBrand.getEmail());

        dataApiClient.put("orgBrand/{id}", obj, obj.getOrgId());
    }


}
