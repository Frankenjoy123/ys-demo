package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.api.dto.PermissionInstance;
import com.yunsoo.api.util.WildcardMatcher;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.PermissionActionObject;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.data.object.PermissionRegionObject;
import com.yunsoo.common.data.object.PermissionResourceObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
@ObjectCacheConfig
@Component
public class PermissionDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private LookupDomain lookupDomain;


    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'resourceList')")
    public List<PermissionResourceObject> getPermissionResources() {
        return dataAPIClient.get("permission/resource", new ParameterizedTypeReference<List<PermissionResourceObject>>() {
        });
    }

    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'actionList')")
    public List<PermissionActionObject> getPermissionActions() {
        return dataAPIClient.get("permission/action", new ParameterizedTypeReference<List<PermissionActionObject>>() {
        });
    }

    //region region

    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'region/' + #id)")
    public PermissionRegionObject getPermissionRegionById(String id) {
        if (id == null || id.length() == 0) return null;
        try {
            return dataAPIClient.get("permission/region/{id}", PermissionRegionObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    //endregion

    //region policy

    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'policyList')")
    public List<PermissionPolicyObject> getPermissionPolicies() {
        return dataAPIClient.get("permission/policy", new ParameterizedTypeReference<List<PermissionPolicyObject>>() {
        });
    }

    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'policyMap')")
    public Map<String, PermissionPolicyObject> getPermissionPolicyMap() {
        Map<String, PermissionPolicyObject> policyMap = new HashMap<>();
        List<PermissionPolicyObject> policyList = getPermissionPolicies();
        policyList.forEach(p -> {
            if (p != null) {
                policyMap.put(p.getCode(), p);
            }
        });

        return policyMap;
    }

    //endregion

    public List<PermissionInstance> extendPermissions(List<PermissionInstance> permissions) {
        List<String> resources = lookupDomain.getLookupListByType(LookupCodes.LookupType.PermissionResource, true)
                .stream().map(Lookup::getCode).collect(Collectors.toList());
        List<PermissionInstance> result = new ArrayList<>();
        permissions.forEach(p -> {
            String resourceCode = p.getResourceCode();
            String actionCode = p.getActionCode();
            String orgId = p.getOrgId();
            if (StringUtils.isEmpty(resourceCode) || StringUtils.isEmpty(actionCode)) {
                return;
            }
            if (!resourceCode.equals("*") && resourceCode.contains("*")) {
                resources.stream().filter(i -> WildcardMatcher.match(resourceCode, i)).forEach(r -> result.add(new PermissionInstance(r, actionCode, orgId)));
            } else {
                result.add(new PermissionInstance(resourceCode, actionCode, orgId));
            }
        });
        return result;
    }
}
