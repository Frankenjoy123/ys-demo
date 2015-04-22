package com.yunsoo.api.security;

import com.yunsoo.api.domain.AccountDomain;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.common.data.object.AccountObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Zhe on 2015/3/5.
 */
@Service
public class AccountDetailService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private AccountDomain accountDomain;
//    @Autowired
//    private UserRepository userRepo;

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public final TAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] splitUsername = username.split(":");
        AccountObject accountObject = accountDomain.getByOrgIdAndIdentifier(splitUsername[0], splitUsername[1]);
        if (accountObject == null) {
            throw new UsernameNotFoundException("user not found");
        }
        TAccount account = new TAccount();
        account.setId(accountObject.getId());
        account.setOrgId(accountObject.getOrgId());

        detailsChecker.check(account);
        return account;
    }
}