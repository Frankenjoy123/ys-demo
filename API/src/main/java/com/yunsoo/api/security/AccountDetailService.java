package com.yunsoo.api.security;

import com.yunsoo.api.object.TAccount;
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

    private RestClient dataAPIClient;

    @Autowired
    AccountDetailService(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }
//    @Autowired
//    private UserRepository userRepo;


    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public final TAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        TAccount account = dataAPIClient.get("account/username/{username}", TAccount.class, username);

        if (account == null) {
            throw new UsernameNotFoundException("user not found");
        }
        detailsChecker.check(account);
        return account;
    }
}