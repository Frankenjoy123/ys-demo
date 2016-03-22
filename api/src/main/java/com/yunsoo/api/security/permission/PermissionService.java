package com.yunsoo.api.security.permission;

import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.PermissionExpression.PolicyPermissionExpression;
import com.yunsoo.api.security.permission.expression.PermissionExpression.SimplePermissionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression.OrgRestrictionExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression.RegionRestrictionExpression;
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


    public List<RestrictionExpression> getRegionRestrictionsByRegionId(String id) {
        List<RestrictionExpression> restrictions = new ArrayList<>();
        PermissionRegionObject permissionRegionObject = permissionDomain.getPermissionRegionById(id);
        if (permissionRegionObject != null && permissionRegionObject.getRestrictions() != null) {
            permissionRegionObject.getRestrictions().forEach(rs -> {
                RestrictionExpression exp = RestrictionExpression.newInstance(rs);
                if (exp != null) {
                    restrictions.add(exp);
                }
            });
        }
        return restrictions;
    }

    public Map<String, List<PermissionExpression>> getPolicyPermissionMap() {
        Map<String, List<PermissionExpression>> policyMap = new HashMap<>();
        List<PermissionPolicyObject> policyList = permissionDomain.getPermissionPolicies();
        policyList.forEach(p -> {
            if (p != null && p.getPermissions() != null && p.getPermissions().size() > 0) {
                List<PermissionExpression> permissions = new ArrayList<>();
                p.getPermissions().forEach(ps -> {
                    PermissionExpression exp = PermissionExpression.newInstance(ps);
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

    //region extends

    public List<OrgRestrictionExpression> extendRegionRestrictionExpression(RegionRestrictionExpression restriction) {
        return extendRegionRestrictionExpression(restriction, 5);
    }

    public List<OrgRestrictionExpression> extendRegionRestrictionExpression(RegionRestrictionExpression restriction, int ttl) {
        if (ttl <= 0) {
            return new ArrayList<>();
        }
        List<OrgRestrictionExpression> orgRestrictions = new ArrayList<>();
        List<RestrictionExpression> restrictions = getRegionRestrictionsByRegionId(restriction.getValue());
        if (restrictions != null) {
            restrictions.forEach(r -> {
                if (r instanceof OrgRestrictionExpression) {
                    orgRestrictions.add((OrgRestrictionExpression) r);
                } else if (!restriction.equals(r)) {
                    orgRestrictions.addAll(extendRegionRestrictionExpression((RegionRestrictionExpression) r, ttl - 1));
                }
            });
        }
        return orgRestrictions.stream().distinct().sorted().collect(Collectors.toList());
    }

    public List<SimplePermissionExpression> extendPolicyPermissionExpression(PolicyPermissionExpression permission, Map<String, List<PermissionExpression>> policyPermissionMap) {
        return extendPolicyPermissionExpression(permission, policyPermissionMap, 5);
    }

    public List<SimplePermissionExpression> extendPolicyPermissionExpression(PolicyPermissionExpression permission, Map<String, List<PermissionExpression>> policyPermissionMap, int ttl) {
        if (ttl <= 0) {
            return new ArrayList<>();
        }
        List<SimplePermissionExpression> simplePermissions = new ArrayList<>();
        List<PermissionExpression> inlinePermissions = policyPermissionMap.get(permission.getValue());
        if (inlinePermissions != null) {
            inlinePermissions.forEach(p -> {
                if (p instanceof SimplePermissionExpression) {
                    simplePermissions.add((SimplePermissionExpression) p);
                } else if (!permission.equals(p)) {
                    simplePermissions.addAll(extendPolicyPermissionExpression((PolicyPermissionExpression) p, policyPermissionMap, ttl - 1));
                }
            });
        }
        return simplePermissions.stream().distinct().sorted().collect(Collectors.toList());
    }

    //endregion

}
