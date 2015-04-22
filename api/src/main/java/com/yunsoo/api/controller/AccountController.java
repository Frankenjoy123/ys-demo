package com.yunsoo.api.controller;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.dto.basic.Account;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TAccountToken;
import com.yunsoo.api.security.AccountAuthentication;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public TAccount getCurrent() {
//        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof AccountAuthentication) {
//            return ((AccountAuthentication) authentication).getDetails();
//        }
//        return new TAccount(authentication.getName()); //anonymous user support
        return null;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Account> getByFilter(@RequestParam("org_id") String orgId) {
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