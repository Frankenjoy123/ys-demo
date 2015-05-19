package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.dto.AccountPassword;
import com.yunsoo.api.dto.basic.Account;
import com.yunsoo.api.security.TokenAuthenticationService;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.exception.UnprocessableEntityException;
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
        if ("current".equals(id)) { //get current Account
            id = tokenAuthenticationService.getAuthentication().getDetails().getId();
        }
        accountObject = accountDomain.getById(id);
        if (accountObject == null) {
            throw new NotFoundException("Account not found by [id: " + id + "]");
        }
        return toAccount(accountObject);
    }

    @RequestMapping(value = "/current/password", method = RequestMethod.POST)
    public void updatePassword(@RequestBody AccountPassword accountPassword) {

        String currentAccountId = tokenAuthenticationService.getAuthentication().getDetails().getId();

        AccountObject accountObject = accountDomain.getById(currentAccountId);

        if (accountObject == null) {
            throw new NotFoundException("Account not found");
        }

        String rawOldPassword = accountPassword.getOldPassword();
        String rawNewPassword = accountPassword.getNewPassword();
        String password = accountObject.getPassword();
        String hashSalt = accountObject.getHashSalt();

        if (!accountDomain.validPassword(rawOldPassword, hashSalt, password)) {
            throw new UnprocessableEntityException("当前密码不匹配");
        }

        accountDomain.updatePassword(currentAccountId, rawNewPassword);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Account> getByFilter(@RequestParam(value = "org_id", required = false) String orgId) {
        if (orgId == null) {
            orgId = tokenAuthenticationService.getAuthentication().getDetails().getOrgId();
        }
        return accountDomain.getByOrgId(orgId).stream().map(this::toAccount).collect(Collectors.toList());
    }


    private Account toAccount(AccountObject accountObject) {
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