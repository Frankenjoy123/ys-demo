package com.yunsoo.api.security.authorization;

import com.yunsoo.api.auth.service.AuthPermissionService;
import com.yunsoo.common.web.security.permission.PermissionEntry;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-03-23
 * Descriptions: referred by AccountAuthentication
 */
@Service
public class AuthorizationService {

    @Autowired
    private AuthPermissionService authPermissionService;

    /**
     * @return PermissionEntry list of the current account
     */
    public List<PermissionEntry> getPermissionEntries() {
        return authPermissionService.getPermissionList()
                .stream()
                .filter(e -> e != null)
                .map(e -> new PermissionEntry(
                        e.getId(),
                        e.getPrincipal(),
                        e.getRestriction(),
                        e.getPermission(),
                        e.getEffect()))
                .filter(PermissionEntry::isValid)
                .collect(Collectors.toList());
    }

    public boolean checkPermission(RestrictionExpression restriction, PermissionExpression permission) {
        return restriction != null && permission != null && authPermissionService.checkPermission(restriction, permission);
    }

}
