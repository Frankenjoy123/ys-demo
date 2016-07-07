package com.yunsoo.auth.service;

import com.yunsoo.auth.dto.Account;
import com.yunsoo.auth.dto.AccountLoginRequest;
import com.yunsoo.auth.dto.Organization;
import com.yunsoo.common.util.ObjectIdGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-07-05
 * Descriptions:
 */
@Service
public class LoginService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrganizationService organizationService;


    public Account login(AccountLoginRequest loginRequest) {
        Account account = null;
        Organization organization = null;

        //find account & organization
        if (StringUtils.hasText(loginRequest.getAccountId())) {
            account = accountService.getById(loginRequest.getAccountId().trim());
            if (account == null) {
                log.warn(String.format("account not found by id, [id: %s]", loginRequest.getAccountId().trim()));
            } else {
                organization = organizationService.getById(account.getOrgId());
                if (organization == null) {
                    log.warn(String.format("organization not found by id, [id: %s]", account.getOrgId()));
                }
            }
        } else if (StringUtils.hasText(loginRequest.getOrganization()) && StringUtils.hasText(loginRequest.getIdentifier())) {
            String org = loginRequest.getOrganization().trim();
            String identifier = loginRequest.getIdentifier().trim();
            if (ObjectIdGenerator.validate(org)) {
                //org is orgId
                organization = organizationService.getById(org);
            }
            if (organization == null) {
                //org is orgName
                organization = organizationService.getByName(org);
            }
            if (organization == null) {
                log.warn(String.format("organization not found, [org: %s]", org));
                return null;
            }
            account = accountService.getByOrgIdAndIdentifier(organization.getId(), identifier);
            if (account == null) {
                log.warn(String.format("account not found, [orgId: %s, identifier: %s]", organization.getId(), identifier));
                return null;
            }

        }

        if (account == null || organization == null) {
            return null;
        }

        //validate account & organization
        if (!accountService.validateAccount(account) || !organizationService.validateOrganization(organization)) {
            log.warn("status of account or organization is not valid");
            return null;
        }

        //validate password
        if (!accountService.validatePassword(loginRequest.getPassword(), account.getHashSalt(), account.getPassword())) {
            log.warn(String.format("password not valid, [id: %s, orgId: %s, identifier: %s]", account.getId(), account.getOrgId(), account.getIdentifier()));
            return null;
        }

        return account;
    }

    public Account login(String accountId) {
        Account account = accountService.getById(accountId);
        Organization organization = account != null ? organizationService.getById(account.getOrgId()) : null;

        if (account == null || organization == null) {
            return null;
        }

        //validate account & organization
        if (!accountService.validateAccount(account) || !organizationService.validateOrganization(organization)) {
            log.warn("status of account or organization is not valid");
            return null;
        }

        return account;
    }

}
