package com.yunsoo.api.security.authorization;

import com.yunsoo.api.domain.PermissionAllocationDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-03-23
 * Descriptions:
 */
@Service
public class AuthorizationService {

    @Autowired
    private PermissionAllocationDomain permissionAllocationDomain;

    public List<AccountGrantedAuthority> getGrantedAuthorities(String accountId) {
        return permissionAllocationDomain.getGrantedAuthoritiesByAccountId(accountId)
                .stream().map(AccountGrantedAuthority::new).collect(Collectors.toList());
    }
}
