package com.yunsoo.api.security.permission;

import com.yunsoo.api.domain.AccountGroupDomain;
import com.yunsoo.api.domain.PermissionAllocationDomain;
import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.PermissionExpression.CollectionPermissionExpression;
import com.yunsoo.api.security.permission.expression.PermissionExpression.PolicyPermissionExpression;
import com.yunsoo.api.security.permission.expression.PermissionExpression.SimplePermissionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression.CollectionRestrictionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression.RegionRestrictionExpression;
import com.yunsoo.common.data.object.AccountGroupObject;
import com.yunsoo.common.data.object.PermissionAllocationObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.data.object.PermissionRegionObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions:
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionDomain permissionDomain;

    @Autowired
    private PermissionAllocationDomain permissionAllocationDomain;

    @Autowired
    private AccountGroupDomain accountGroupDomain;

    public List<PermissionEntry> getExpendedPermissionEntriesByAccountId(String accountId) {
        List<PermissionEntry> permissionEntries = getPermissionEntriesByAccountId(accountId);
        Map<String, List<PermissionExpression>> policyPermissionMap = getPolicyPermissionMap();
        permissionEntries.forEach(p -> {
            expendPermissionEntry(p, policyPermissionMap);
        });
        return permissionEntries.stream().filter(PermissionEntry::isValid).sorted().collect(Collectors.toList());
    }

    public List<PermissionEntry> getPermissionEntriesByAccountId(String accountId) {
        List<PermissionEntry> permissionEntries = new ArrayList<>();
        List<String> groupIds = accountGroupDomain.getAccountGroupByAccountId(accountId)
                .stream().map(AccountGroupObject::getGroupId).collect(Collectors.toList());
        List<PermissionAllocationObject> paObjects = permissionAllocationDomain.getPermissionAllocations(accountId, groupIds);
        paObjects.forEach(pa -> {
            if (pa != null) permissionEntries.add(new PermissionEntry(pa));
        });
        return permissionEntries.stream().filter(PermissionEntry::isValid).sorted().collect(Collectors.toList());
    }

    private void expendPermissionEntry(PermissionEntry permissionEntry, Map<String, List<PermissionExpression>> policyPermissionMap) {
        List<OrgRestrictionExpression> restrictions = expendRestrictionExpression(permissionEntry.getRestriction());
        RestrictionExpression restriction = RestrictionExpression.collect(restrictions);
        permissionEntry.setRestriction(restriction);

        List<SimplePermissionExpression> permissions = expendPermissionExpression(permissionEntry.getPermission(), policyPermissionMap);
        PermissionExpression permission = PermissionExpression.collect(permissions);
        permissionEntry.setPermission(permission);
    }

    private List<RestrictionExpression> getRegionRestrictionsByRegionId(String id) {
        List<RestrictionExpression> restrictions = new ArrayList<>();
        PermissionRegionObject permissionRegionObject = permissionDomain.getPermissionRegionById(id);
        if (permissionRegionObject != null && permissionRegionObject.getRestrictions() != null) {
            permissionRegionObject.getRestrictions().forEach(rs -> {
                RestrictionExpression exp = RestrictionExpression.parse(rs);
                if (exp != null) {
                    restrictions.add(exp);
                }
            });
        }
        return restrictions;
    }

    private Map<String, List<PermissionExpression>> getPolicyPermissionMap() {
        Map<String, List<PermissionExpression>> policyMap = new HashMap<>();
        List<PermissionPolicyObject> policyList = permissionDomain.getPermissionPolicies();
        policyList.forEach(p -> {
            if (p != null && p.getPermissions() != null && p.getPermissions().size() > 0) {
                List<PermissionExpression> permissions = new ArrayList<>();
                p.getPermissions().forEach(ps -> {
                    PermissionExpression exp = PermissionExpression.parse(ps);
                    if (exp != null) {
                        permissions.add(exp);
                    }
                });
                if (permissions.size() > 0) {
                    policyMap.put(p.getCode(), permissions);
                }
            }
        });
        return policyMap;
    }


    //region expends

    private List<OrgRestrictionExpression> expendRestrictionExpression(RestrictionExpression restriction) {
        return expendRestrictionExpression(restriction, 5);
    }

    private List<OrgRestrictionExpression> expendRestrictionExpression(RestrictionExpression restriction, int ttl) {
        if (ttl <= 0) {
            return new ArrayList<>();
        }
        List<OrgRestrictionExpression> orgRestrictions = new ArrayList<>();
        if (restriction instanceof OrgRestrictionExpression) {
            orgRestrictions.add((OrgRestrictionExpression) restriction);
        } else {
            List<RestrictionExpression> inlineRestrictions = null;
            if (restriction instanceof RegionRestrictionExpression) {
                inlineRestrictions = getRegionRestrictionsByRegionId(restriction.getValue());
            } else if (restriction instanceof CollectionRestrictionExpression) {
                inlineRestrictions = ((CollectionRestrictionExpression) restriction).getExpressions();
            }
            if (inlineRestrictions != null) {
                inlineRestrictions.forEach(r -> {
                    if (r != null && !r.equals(restriction)) {
                        orgRestrictions.addAll(expendRestrictionExpression(r, ttl - 1));
                    }
                });
            }
        }
        return orgRestrictions.size() <= 1 ? orgRestrictions : orgRestrictions.stream().distinct().sorted().collect(Collectors.toList());
    }


    private List<SimplePermissionExpression> expendPermissionExpression(PermissionExpression permission, Map<String, List<PermissionExpression>> policyPermissionMap) {
        return expendPermissionExpression(permission, policyPermissionMap, 5);
    }

    private List<SimplePermissionExpression> expendPermissionExpression(PermissionExpression permission, Map<String, List<PermissionExpression>> policyPermissionMap, int ttl) {
        if (ttl <= 0) {
            return new ArrayList<>();
        }
        List<SimplePermissionExpression> simplePermissions = new ArrayList<>();
        if (permission instanceof SimplePermissionExpression) {
            simplePermissions.add((SimplePermissionExpression) permission);
        } else {
            List<PermissionExpression> inlinePermissions = null;
            if (permission instanceof PolicyPermissionExpression) {
                inlinePermissions = policyPermissionMap.get(permission.getValue());
            } else if (permission instanceof CollectionPermissionExpression) {
                inlinePermissions = ((CollectionPermissionExpression) permission).getExpressions();
            }
            if (inlinePermissions != null) {
                inlinePermissions.forEach(p -> {
                    if (p != null && !p.equals(permission)) {
                        simplePermissions.addAll(expendPermissionExpression(p, policyPermissionMap, ttl - 1));
                    }
                });
            }
        }
        return simplePermissions.size() <= 1 ? simplePermissions : simplePermissions.stream().distinct().sorted().collect(Collectors.toList());
    }

    //endregion

}
