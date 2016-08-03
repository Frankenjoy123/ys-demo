package com.yunsoo.auth.api.security.permission;

import com.yunsoo.auth.dto.PermissionAllocation;
import com.yunsoo.auth.dto.PermissionPolicy;
import com.yunsoo.auth.dto.PermissionRegion;
import com.yunsoo.auth.service.PermissionAllocationService;
import com.yunsoo.auth.service.PermissionRegionService;
import com.yunsoo.auth.service.PermissionService;
import com.yunsoo.common.web.security.permission.PermissionEntry;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression.CollectionPermissionExpression;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression.PolicyPermissionExpression;
import com.yunsoo.common.web.security.permission.expression.PermissionExpression.SimplePermissionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.CollectionRestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression.RegionRestrictionExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-07-11
 * Descriptions:
 */
@Service
public class PermissionEntryService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionRegionService permissionRegionService;

    @Autowired
    private PermissionAllocationService permissionAllocationService;


    //todo: add cached logic
    public List<String> getExpendedPermissionEntriesByAccountIdCached(String accountId) {
        return this.getExpendedPermissionEntriesByAccountId(accountId).stream()
                .map(PermissionEntry::toString)
                .collect(Collectors.toList());
    }

    public List<PermissionEntry> getExpendedPermissionEntriesByAccountId(String accountId) {
        List<PermissionEntry> permissionEntries = getPermissionEntriesByAccountId(accountId);
        Map<String, List<PermissionExpression>> policyPermissionMap = getPolicyPermissionMap();
        permissionEntries.forEach(p -> {
            expendPermissionEntry(p, policyPermissionMap);
        });
        return permissionEntries.stream().filter(PermissionEntry::isValid).sorted().collect(Collectors.toList());
    }

    public List<PermissionEntry> getExpendedPermissionEntriesByGroupId(String groupId) {
        List<PermissionEntry> permissionEntries = getPermissionEntriesByGroupId(groupId);
        Map<String, List<PermissionExpression>> policyPermissionMap = getPolicyPermissionMap();
        permissionEntries.forEach(p -> {
            expendPermissionEntry(p, policyPermissionMap);
        });
        return permissionEntries.stream().filter(PermissionEntry::isValid).sorted().collect(Collectors.toList());
    }

    //region private methods

    private List<PermissionEntry> getPermissionEntriesByAccountId(String accountId) {
        List<PermissionAllocation> paObjects = permissionAllocationService.getAllPermissionAllocationsByAccountId(accountId);
        return paObjects.stream()
                .map(p -> new PermissionEntry(p.getId(), p.getPrincipal(), p.getRestriction(), p.getPermission(), p.getEffect()))
                .filter(PermissionEntry::isValid).sorted().collect(Collectors.toList());
    }

    private List<PermissionEntry> getPermissionEntriesByGroupId(String groupId) {
        List<PermissionAllocation> paObjects = permissionAllocationService.getPermissionAllocationsByGroupId(groupId);
        return paObjects.stream()
                .map(p -> new PermissionEntry(p.getId(), p.getPrincipal(), p.getRestriction(), p.getPermission(), p.getEffect()))
                .filter(PermissionEntry::isValid).sorted().collect(Collectors.toList());
    }

    private List<RestrictionExpression> getRegionRestrictionsByRegionId(String id) {
        List<RestrictionExpression> restrictions = new ArrayList<>();
        PermissionRegion permissionRegionObject = permissionRegionService.getById(id);
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
        List<PermissionPolicy> policyList = permissionService.getPermissionPolicies();
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

    private void expendPermissionEntry(PermissionEntry permissionEntry, Map<String, List<PermissionExpression>> policyPermissionMap) {
        List<OrgRestrictionExpression> restrictions = expendRestrictionExpression(permissionEntry.getRestriction());
        RestrictionExpression restriction = RestrictionExpression.collect(restrictions);
        permissionEntry.setRestriction(restriction);

        List<SimplePermissionExpression> permissions = expendPermissionExpression(permissionEntry.getPermission(), policyPermissionMap);
        PermissionExpression permission = PermissionExpression.collect(permissions);
        permissionEntry.setPermission(permission);
    }

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

    //endregion

}
