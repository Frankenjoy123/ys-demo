package com.yunsoo.api.controller;

import com.yunsoo.api.auth.dto.Account;
import com.yunsoo.api.auth.dto.AccountCreationRequest;
import com.yunsoo.api.auth.dto.Organization;
import com.yunsoo.api.auth.service.AuthAccountService;
import com.yunsoo.api.auth.service.AuthOrganizationService;
import com.yunsoo.api.auth.service.AuthPermissionService;
import com.yunsoo.api.util.AuthUtils;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by:   Lijian
 * Created on:   2016-08-10
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/organization/carrier")
public class OrganizationCarrierController {

    @Autowired
    private AuthOrganizationService authOrganizationService;

    @Autowired
    private AuthAccountService authAccountService;

    @Autowired
    private AuthPermissionService authPermissionService;


    @RequestMapping(value = "{id}/account", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#carrierId, 'org', 'account:create')")
    public Account createAccount(@PathVariable("id") String carrierId,
                                 @RequestBody AccountCreationRequest request) {
        carrierId = AuthUtils.fixOrgId(carrierId);
        Organization organization = authOrganizationService.getById(carrierId);
        if (organization == null || LookupCodes.OrgType.BRAND.equals(organization.getTypeCode())) {
            throw new NotFoundException("organization carrier not found by id: " + carrierId);
        }
        request.setOrgId(carrierId);
        request.setHashSalt(null);
        request.setCreatedAccountId(null);
        Account account = authAccountService.create(request);
        authPermissionService.allocateAdminPermissionOnCurrentOrgToAccount(account.getId());
        authPermissionService.allocateAdminPermissionOnDefaultRegionToAccount(account.getId());
        return account;
    }

}
