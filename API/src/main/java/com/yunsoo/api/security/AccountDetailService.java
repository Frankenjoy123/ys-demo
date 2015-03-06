package com.yunsoo.api.security;

import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TAccountRole;
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

//    @Autowired
//    private UserRepository userRepo;

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public final TAccount loadUserByUsername(String username) throws UsernameNotFoundException {
        final TAccount tAccount = new TAccount(); // userRepo.findByUsername(username);
        //mock up TAccount - to be updated by Kaibin
        tAccount.setUsername("YunsooAdmin");
        tAccount.setPassword(new BCryptPasswordEncoder().encode("12345678"));
        tAccount.grantRole(TAccountRole.YUNSOO_ADMIN);
//        Set<TAccountRole> accountRoles = new HashSet<TAccountRole>();
//        accountRoles.add(TAccountRole.YUNSOO_ADMIN);
//        tAccount.setRoles(accountRoles);

        if (tAccount == null) {
            throw new UsernameNotFoundException("user not found");
        }
        detailsChecker.check(tAccount);
        return tAccount;
    }
}