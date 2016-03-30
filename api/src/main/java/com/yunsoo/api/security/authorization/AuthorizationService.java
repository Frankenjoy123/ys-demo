package com.yunsoo.api.security.authorization;

import com.yunsoo.api.domain.PermissionAllocationDomain;
import com.yunsoo.api.security.permission.PermissionEntry;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
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


    public List<PermissionEntry> getPermissionEntries(String accountId) {
        return permissionAllocationDomain.getPermissionEntriesByAccountId(accountId)
                .stream().map(PermissionEntry::parse).collect(Collectors.toList());
    }

    /**
     * main entrance to the checkPermission logic
     *
     * @param permissionEntries List<PermissionEntry>
     * @param restriction       [OrgRestrictionExpression|CollectionRestrictionExpression]
     * @param permission        [SimplePermissionExpression|CollectionPermissionExpression]
     * @return boolean    [true: has permission, false: has no permission]
     */
    public boolean checkPermission(List<PermissionEntry> permissionEntries, RestrictionExpression restriction, PermissionExpression permission) {
        if (permissionEntries == null || permissionEntries.size() == 0 || restriction == null || permission == null) {
            return false;
        }
        if (restriction instanceof RestrictionExpression.CollectionRestrictionExpression) {
            for (RestrictionExpression r : ((RestrictionExpression.CollectionRestrictionExpression) restriction).distinct().getExpressions()) {
                if (!checkPermission(permissionEntries, r, permission)) {
                    return false;
                }
            }
            return true;
        }
        if (permission instanceof PermissionExpression.CollectionPermissionExpression) {
            for (PermissionExpression p : ((PermissionExpression.CollectionPermissionExpression) permission).distinct().getExpressions()) {
                if (!checkPermission(permissionEntries, restriction, p)) {
                    return false;
                }
            }
            return true;
        }
        for (PermissionEntry entry : permissionEntries) {
            PermissionEntry.Effect effect = entry.check(restriction, permission);
            if (effect != null) {
                return effect.equals(PermissionEntry.Effect.allow);
            }
        }
        return false;
    }
}
