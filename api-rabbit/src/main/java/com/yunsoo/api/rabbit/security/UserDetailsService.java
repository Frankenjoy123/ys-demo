package com.yunsoo.api.rabbit.security;

import com.yunsoo.api.rabbit.domain.UserDomain;
import com.yunsoo.api.rabbit.object.TUser;
import com.yunsoo.common.data.object.UserObject;
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
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserDomain userDomain;


    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public final TUser loadUserByUsername(String username) throws UsernameNotFoundException {

        UserObject userObject = userDomain.getUserById(username);
        if (userObject == null) {
            throw new UsernameNotFoundException("user not found");
        }
        TUser tUser = new TUser();
        tUser.setId(userObject.getId());

        detailsChecker.check(tUser);
        return tUser;
    }
}