package com.yunsoo.api.security;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.common.data.object.AccountObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by:   Zhe
 * Created on:   2015/3/5
 * Descriptions:
 */
@Service
public class AccountDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private AccountDomain accountDomain;


    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public final TAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("AccountDetailService.loadUserByUsername is invoked");

        AccountObject accountObject = accountDomain.getById(username);
        if (accountObject == null) {
            throw new UsernameNotFoundException("account not found");
        }
        TAccount tAccount = new TAccount();
        tAccount.setId(accountObject.getId());
        tAccount.setOrgId(accountObject.getOrgId());

        detailsChecker.check(tAccount);
        return tAccount;
    }
}