package com.yunsoo.auth.service;

import org.springframework.stereotype.Service;

/**
 * Created by:   Lijian
 * Created on:   2016-07-06
 * Descriptions:
 */
@Service
public class PermissionService {

//    @Autowired
//    private PermissionDomain permissionDomain;
//
//    @Autowired
//    private PermissionAllocationDomain permissionAllocationDomain;

//
//    public List<PermissionEntry> getExpendedPermissionEntriesByAccountId(String accountId) {
//        List<PermissionEntry> permissionEntries = getPermissionEntriesByAccountId(accountId);
//        Map<String, List<PermissionExpression>> policyPermissionMap = getPolicyPermissionMap();
//        permissionEntries.forEach(p -> {
//            expendPermissionEntry(p, policyPermissionMap);
//        });
//        return permissionEntries.stream().filter(PermissionEntry::isValid).sorted().collect(Collectors.toList());
//    }
//
//    public List<PermissionEntry> getExpendedPermissionEntriesByGroupId(String groupId) {
//        List<PermissionEntry> permissionEntries = getPermissionEntriesByGroupId(groupId);
//        Map<String, List<PermissionExpression>> policyPermissionMap = getPolicyPermissionMap();
//        permissionEntries.forEach(p -> {
//            expendPermissionEntry(p, policyPermissionMap);
//        });
//        return permissionEntries.stream().filter(PermissionEntry::isValid).sorted().collect(Collectors.toList());
//    }
//
//
//    private List<PermissionEntry> getPermissionEntriesByAccountId(String accountId) {
//        List<PermissionAllocationObject> paObjects = permissionAllocationDomain.getAllPermissionAllocationsByAccountId(accountId);
//        return paObjects.stream()
//                .map(PermissionEntry::new)
//                .filter(PermissionEntry::isValid).sorted().collect(Collectors.toList());
//    }
//
//    private List<PermissionEntry> getPermissionEntriesByGroupId(String groupId) {
//        List<PermissionAllocationObject> paObjects = permissionAllocationDomain.getPermissionAllocationsByGroupId(groupId);
//        return paObjects.stream()
//                .map(PermissionEntry::new)
//                .filter(PermissionEntry::isValid).sorted().collect(Collectors.toList());
//    }
//
//    private void expendPermissionEntry(PermissionEntry permissionEntry, Map<String, List<PermissionExpression>> policyPermissionMap) {
//        List<RestrictionExpression.OrgRestrictionExpression> restrictions = expendRestrictionExpression(permissionEntry.getRestriction());
//        RestrictionExpression restriction = RestrictionExpression.collect(restrictions);
//        permissionEntry.setRestriction(restriction);
//
//        List<PermissionExpression.SimplePermissionExpression> permissions = expendPermissionExpression(permissionEntry.getPermission(), policyPermissionMap);
//        PermissionExpression permission = PermissionExpression.collect(permissions);
//        permissionEntry.setPermission(permission);
//    }
//
//    private List<RestrictionExpression> getRegionRestrictionsByRegionId(String id) {
//        List<RestrictionExpression> restrictions = new ArrayList<>();
//        PermissionRegionObject permissionRegionObject = permissionDomain.getPermissionRegionById(id);
//        if (permissionRegionObject != null && permissionRegionObject.getRestrictions() != null) {
//            permissionRegionObject.getRestrictions().forEach(rs -> {
//                RestrictionExpression exp = RestrictionExpression.parse(rs);
//                if (exp != null) {
//                    restrictions.add(exp);
//                }
//            });
//        }
//        return restrictions;
//    }
//
//    private Map<String, List<PermissionExpression>> getPolicyPermissionMap() {
//        Map<String, List<PermissionExpression>> policyMap = new HashMap<>();
//        List<PermissionPolicyObject> policyList = permissionDomain.getPermissionPolicies();
//        policyList.forEach(p -> {
//            if (p != null && p.getPermissions() != null && p.getPermissions().size() > 0) {
//                List<PermissionExpression> permissions = new ArrayList<>();
//                p.getPermissions().forEach(ps -> {
//                    PermissionExpression exp = PermissionExpression.parse(ps);
//                    if (exp != null) {
//                        permissions.add(exp);
//                    }
//                });
//                if (permissions.size() > 0) {
//                    policyMap.put(p.getCode(), permissions);
//                }
//            }
//        });
//        return policyMap;
//    }


    //region expends
//
//    private List<RestrictionExpression.OrgRestrictionExpression> expendRestrictionExpression(RestrictionExpression restriction) {
//        return expendRestrictionExpression(restriction, 5);
//    }
//
//    private List<RestrictionExpression.OrgRestrictionExpression> expendRestrictionExpression(RestrictionExpression restriction, int ttl) {
//        if (ttl <= 0) {
//            return new ArrayList<>();
//        }
//        List<RestrictionExpression.OrgRestrictionExpression> orgRestrictions = new ArrayList<>();
//        if (restriction instanceof RestrictionExpression.OrgRestrictionExpression) {
//            orgRestrictions.add((RestrictionExpression.OrgRestrictionExpression) restriction);
//        } else {
//            List<RestrictionExpression> inlineRestrictions = null;
//            if (restriction instanceof RestrictionExpression.RegionRestrictionExpression) {
//                inlineRestrictions = getRegionRestrictionsByRegionId(restriction.getValue());
//            } else if (restriction instanceof RestrictionExpression.CollectionRestrictionExpression) {
//                inlineRestrictions = ((RestrictionExpression.CollectionRestrictionExpression) restriction).getExpressions();
//            }
//            if (inlineRestrictions != null) {
//                inlineRestrictions.forEach(r -> {
//                    if (r != null && !r.equals(restriction)) {
//                        orgRestrictions.addAll(expendRestrictionExpression(r, ttl - 1));
//                    }
//                });
//            }
//        }
//        return orgRestrictions.size() <= 1 ? orgRestrictions : orgRestrictions.stream().distinct().sorted().collect(Collectors.toList());
//    }
//
//
//    private List<PermissionExpression.SimplePermissionExpression> expendPermissionExpression(PermissionExpression permission, Map<String, List<PermissionExpression>> policyPermissionMap) {
//        return expendPermissionExpression(permission, policyPermissionMap, 5);
//    }
//
//    private List<PermissionExpression.SimplePermissionExpression> expendPermissionExpression(PermissionExpression permission, Map<String, List<PermissionExpression>> policyPermissionMap, int ttl) {
//        if (ttl <= 0) {
//            return new ArrayList<>();
//        }
//        List<PermissionExpression.SimplePermissionExpression> simplePermissions = new ArrayList<>();
//        if (permission instanceof PermissionExpression.SimplePermissionExpression) {
//            simplePermissions.add((PermissionExpression.SimplePermissionExpression) permission);
//        } else {
//            List<PermissionExpression> inlinePermissions = null;
//            if (permission instanceof PermissionExpression.PolicyPermissionExpression) {
//                inlinePermissions = policyPermissionMap.get(permission.getValue());
//            } else if (permission instanceof PermissionExpression.CollectionPermissionExpression) {
//                inlinePermissions = ((PermissionExpression.CollectionPermissionExpression) permission).getExpressions();
//            }
//            if (inlinePermissions != null) {
//                inlinePermissions.forEach(p -> {
//                    if (p != null && !p.equals(permission)) {
//                        simplePermissions.addAll(expendPermissionExpression(p, policyPermissionMap, ttl - 1));
//                    }
//                });
//            }
//        }
//        return simplePermissions.size() <= 1 ? simplePermissions : simplePermissions.stream().distinct().sorted().collect(Collectors.toList());
//    }

    //endregion

}
