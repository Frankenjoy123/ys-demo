package com.yunsoo.api.domain;

import com.yunsoo.api.Constants;
import com.yunsoo.api.auth.dto.Account;
import com.yunsoo.api.auth.dto.AccountCreationRequest;
import com.yunsoo.api.auth.dto.Organization;
import com.yunsoo.api.auth.service.AuthAccountService;
import com.yunsoo.api.auth.service.AuthOrganizationService;
import com.yunsoo.api.auth.service.AuthPermissionService;
import com.yunsoo.api.client.DataApiClient;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.BrandApplicationHistoryObject;
import com.yunsoo.common.data.object.BrandApplicationObject;
import com.yunsoo.common.data.object.OrgBrandObject;
import com.yunsoo.common.util.HashUtils;
import com.yunsoo.common.util.RandomUtils;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.ConflictException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   yan
 * Created on:   3/17/2016
 * Descriptions:
 */
@Component
public class BrandApplicationDomain {

    @Autowired
    private DataApiClient dataApiClient;

    @Autowired
    private OrganizationBrandDomain organizationBrandDomain;

    @Autowired
    private AuthAccountService authAccountService;

    @Autowired
    private AuthPermissionService authPermissionService;

    @Autowired
    private AuthOrganizationService authOrganizationService;


    public BrandApplicationObject getBrandApplicationById(String brandApplicationId) {
        if (StringUtils.isEmpty(brandApplicationId)) {
            return null;
        }
        try {
            return dataApiClient.get("brandApplication/{id}", BrandApplicationObject.class, brandApplicationId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public BrandApplicationObject getBrandApplicationByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        List<BrandApplicationObject> brandApplicationObjects = getBrandApplicationPaged(null, name, null, null, null, null, null, null).getContent();
        if (brandApplicationObjects.size() == 0) {
            return null;
        }
        return brandApplicationObjects.get(0);
    }

    public Page<BrandApplicationObject> getBrandApplicationPaged(
            String carrierId,
            String brandName,
            String statusCode,
            Boolean hasPayment,
            DateTime createdDateTimeGE,
            DateTime createdDateTimeLE,
            String searchText,
            Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("carrier_id", carrierId)
                .append("brand_name", brandName)
                .append("status_code", statusCode)
                .append("has_payment", hasPayment)
                .append("created_datetime_ge", createdDateTimeGE)
                .append("created_datetime_le", createdDateTimeLE)
                .append("search_text", searchText)
                .append(pageable)
                .build();
        return dataApiClient.getPaged("brandApplication" + query, new ParameterizedTypeReference<List<BrandApplicationObject>>() {
        });
    }

    public List<BrandApplicationHistoryObject> getBrandApplicationHistory(String brandApplicationId) {
        return dataApiClient.get("brandApplication/{id}/history", new ParameterizedTypeReference<List<BrandApplicationHistoryObject>>() {
        }, brandApplicationId);
    }

    public BrandApplicationObject createBrandApplication(BrandApplicationObject obj) {
        if (authOrganizationService.getByName(obj.getBrandName()) != null) {
            throw new ConflictException("organization exists with the same name");
        }
        if (getBrandApplicationByName(obj.getBrandName()) != null) {
            throw new ConflictException("brandApplication exists with the same name");
        }
        obj.setId(null);
        obj.setCreatedDateTime(DateTime.now());
        String hashSalt = RandomUtils.generateString(8);
        obj.setPassword(hashPassword(obj.getPassword(), hashSalt));
        obj.setHashSalt(hashSalt);
        if (!StringUtils.isEmpty(obj.getAttachment())) {
            List<String> attachmentIds = Arrays.asList(StringUtils.commaDelimitedListToStringArray(obj.getAttachment()))
                    .stream()
                    .filter(s -> !StringUtils.isEmpty(s))
                    .collect(Collectors.toList());
            obj.setAttachment(StringUtils.collectionToCommaDelimitedString(attachmentIds));
        }
        return dataApiClient.post("brandApplication", obj, BrandApplicationObject.class);
    }

    public void patchUpdateBrandApplication(BrandApplicationObject obj) {
        BrandApplicationObject currObj = getBrandApplicationById(obj.getId());
        if (currObj == null) {
            throw new NotFoundException("brandApplication not found by id: " + obj.getId());
        }
        if (LookupCodes.BrandApplicationStatus.APPROVED.equals(currObj.getStatusCode())) {
            throw new BadRequestException("could not update with status approved");
        }

        if (obj.getBrandDesc() != null) currObj.setBrandDesc(obj.getBrandDesc());
        if (obj.getContactName() != null) currObj.setContactName(obj.getContactName());
        if (obj.getContactMobile() != null) currObj.setContactMobile(obj.getContactMobile());
        if (obj.getEmail() != null) currObj.setEmail(obj.getEmail());
        if (obj.getBusinessLicenseNumber() != null) currObj.setBusinessLicenseNumber(obj.getBusinessLicenseNumber());
        if (obj.getBusinessLicenseStart() != null) currObj.setBusinessLicenseStart(obj.getBusinessLicenseStart());
        if (obj.getBusinessLicenseEnd() != null) currObj.setBusinessLicenseEnd(obj.getBusinessLicenseEnd());
        if (obj.getBusinessSphere() != null) currObj.setBusinessSphere(obj.getBusinessSphere());
        if (obj.getComments() != null) currObj.setComments(obj.getComments());
        if (obj.getAttachment() != null) currObj.setAttachment(obj.getAttachment());
        if (obj.getInvestigatorAttachment() != null) currObj.setInvestigatorAttachment(obj.getInvestigatorAttachment());
        if (obj.getInvestigatorComments() != null) currObj.setInvestigatorComments(obj.getInvestigatorComments());
        if (obj.getRejectReason() != null) currObj.setRejectReason(obj.getRejectReason());
        if (obj.getCategoryId() != null) currObj.setCategoryId(obj.getCategoryId());
        if (!StringUtils.isEmpty(obj.getPassword())) {
            currObj.setPassword(hashPassword(obj.getPassword(), currObj.getHashSalt()));
        }
        try {
            String currentAccountId = AuthUtils.getCurrentAccount().getId();
            currObj.setCreatedAccountId(currentAccountId);
            if (currentAccountId == null) {
                currObj.setStatusCode(LookupCodes.BrandApplicationStatus.CREATED);//back to init status when update by applicant
            }
        } catch (UnauthorizedException ignored) {
        }
        updateBrandApplication(currObj);
    }

    public void updateBrandApplication(BrandApplicationObject obj) {
        if (obj != null && !StringUtils.isEmpty(obj.getId())) {
            dataApiClient.put("brandApplication/{id}", obj, obj.getId());
        }
    }

    public void setPaymentId(String brandApplicationId, String paymentId) {
        BrandApplicationObject obj = getBrandApplicationById(brandApplicationId);
        if (obj != null) {
            obj.setPaymentId(paymentId);
            dataApiClient.put("brandApplication/{id}", obj, brandApplicationId);
        }
    }

    public int count(String carrierId, String statusCode, boolean hasPayment) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("carrier_id", carrierId)
                .append("status_code", statusCode)
                .append("has_payment", hasPayment)
                .build();
        return dataApiClient.get("brandApplication/count/" + query, Integer.class);
    }

    public BrandApplicationObject approveBrandApplication(String brandApplicationId, String categoryId) {
        BrandApplicationObject obj = getBrandApplicationById(brandApplicationId);
        if (obj == null) {
            throw new NotFoundException("brandApplication not found by id: " + brandApplicationId);
        }
        if (!StringUtils.isEmpty(categoryId)) {
            obj.setCategoryId(categoryId);
        }
        String orgId = obj.getId();
        OrgBrandObject orgBrandObject = new OrgBrandObject();
        orgBrandObject.setOrgName(obj.getBrandName());
        orgBrandObject.setContactName(obj.getContactName());
        orgBrandObject.setContactMobile(obj.getContactMobile());
        orgBrandObject.setEmail(obj.getEmail());
        orgBrandObject.setBusinessLicenseNumber(obj.getBusinessLicenseNumber());
        orgBrandObject.setBusinessLicenseStart(obj.getBusinessLicenseStart());
        orgBrandObject.setBusinessLicenseEnd(obj.getBusinessLicenseEnd());
        orgBrandObject.setBusinessSphere(obj.getBusinessSphere());
        orgBrandObject.setComments(obj.getComments());
        orgBrandObject.setCarrierId(obj.getCarrierId());
        orgBrandObject.setAttachment(obj.getAttachment());
        orgBrandObject.setCarrierId(obj.getCarrierId());

        authPermissionService.addOrgIdToDefaultRegion(orgId);
        Organization organization = organizationBrandDomain.createOrganizationBrand(orgId, obj.getBrandName(), obj.getBrandDesc(), orgBrandObject);

        AccountCreationRequest accountCreationRequest = new AccountCreationRequest();
        accountCreationRequest.setOrgId(organization.getId());
        accountCreationRequest.setIdentifier(obj.getIdentifier());
        accountCreationRequest.setFirstName(obj.getContactName());
        accountCreationRequest.setLastName("");
        accountCreationRequest.setEmail(obj.getEmail());
        accountCreationRequest.setPhone(obj.getContactMobile());
        accountCreationRequest.setPassword(obj.getPassword());
        accountCreationRequest.setHashSalt(obj.getHashSalt());
        accountCreationRequest.setCreatedAccountId(Constants.Ids.SYSTEM_ACCOUNT_ID);

        Account account = authAccountService.create(accountCreationRequest);
        authPermissionService.allocateAdminPermissionOnCurrentOrgToAccount(account.getId());

        obj.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        obj.setStatusCode(LookupCodes.BrandApplicationStatus.APPROVED);
        updateBrandApplication(obj);

        return obj;
    }

    public BrandApplicationObject rejectBrandApplication(String brandApplicationId, String rejectReason) {
        BrandApplicationObject obj = getBrandApplicationById(brandApplicationId);
        if (obj == null) {
            throw new NotFoundException("brandApplication not found by id: " + brandApplicationId);
        }
        obj.setStatusCode(LookupCodes.BrandApplicationStatus.REJECTED);
        if (!StringUtils.isEmpty(rejectReason)) {
            obj.setRejectReason(rejectReason);
        }
        obj.setCreatedAccountId(AuthUtils.getCurrentAccount().getId());
        updateBrandApplication(obj);

        return obj;
    }

    public boolean validatePassword(String rawPassword, String hashSalt, String password) {
        return rawPassword != null && rawPassword.length() > 0
                && hashSalt != null && hashPassword(rawPassword, hashSalt).equals(password);
    }

    private String hashPassword(String rawPassword, String hashSalt) {
        return HashUtils.sha1HexString(rawPassword + hashSalt);
    }

}
