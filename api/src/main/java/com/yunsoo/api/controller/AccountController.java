package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.dto.basic.Account;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zhe on 2015/3/5.
 * Handle with accounts which consumes this API.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountDomain accountDomain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Account getById(@PathVariable("id") String id) {
        AccountObject accountObject;
        if ("current".equals(id)) {
            id = tokenAuthenticationService.getAuthentication().getDetails().getId();
        }
        try {
            accountObject = accountDomain.getById(id);
        } catch (NotFoundException ex) {
            throw new NotFoundException("Account not found by [id: " + id + "]");
        }
        return fromAccountObject(accountObject);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Account> getByFilter(@RequestParam(value = "org_id", required = false) String orgId) {
        if (orgId == null) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return accountDomain.getByOrgId(orgId).stream().map(this::fromAccountObject).collect(Collectors.toList());
    }


    private Account fromAccountObject(AccountObject accountObject) {
        if (accountObject == null) {
            return null;
        }
        Account account = new Account();
        account.setId(accountObject.getId());
        account.setOrgId(accountObject.getOrgId());
        account.setIdentifier(accountObject.getIdentifier());
        account.setStatusCode(accountObject.getStatusCode());
        account.setEmail(accountObject.getEmail());
        account.setFirstName(accountObject.getFirstName());
        account.setLastName(accountObject.getLastName());
        account.setPhone(accountObject.getPhone());
        account.setModifiedAccountId(accountObject.getModifiedAccountId());
        account.setModifiedDatetime(accountObject.getModifiedDatetime());
        account.setCreatedAccountId(accountObject.getCreatedAccountId());
        account.setCreatedDateTime(accountObject.getCreatedDateTime());
        return account;
    }

}