package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ElastiCacheConfig;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.api.dto.PermissionInstance;
import com.yunsoo.api.util.WildcardMatcher;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@ElastiCacheConfig
@Component
public class PermissionDomain {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionDomain.class);

    public PermissionDomain(){
        LOGGER.debug("init PermissionDomain");
    }

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private LookupDomain lookupDomain;

    @Cacheable(key="T(com.yunsoo.api.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'list')")
    public List<PermissionPolicyObject> getPermissionPolicies() {
        LOGGER.debug("cache missed [name: permission, key: 'policylist']");
        return dataAPIClient.get("permission/policy", new ParameterizedTypeReference<List<PermissionPolicyObject>>() {
        });
    }

    @Cacheable(key="T(com.yunsoo.api.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'map')")
    public Map<String, PermissionPolicyObject> getPermissionPolicyMap() {
        LOGGER.debug("cache missed [name: permission, key: 'policymap']");
        Map<String, PermissionPolicyObject> permissionPolicies = new HashMap<>();
        //avoid Spring AOP proxy not work for internal invoke
        List<PermissionPolicyObject> policyList =  dataAPIClient.get("permission/policy", new ParameterizedTypeReference<List<PermissionPolicyObject>>() {
        });
        policyList.forEach(o -> {
            if (o == null) {
                return;
            }
            if (o.getPermissions() == null) {
                o.setPermissions(new ArrayList<>());
            }
            String pCode = o.getCode();
            if (permissionPolicies.containsKey(pCode)) {
                permissionPolicies.get(pCode).getPermissions().addAll(o.getPermissions());
            } else {
                permissionPolicies.put(pCode, o);
            }
        });

        return permissionPolicies;
    }


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
                resources.stream().filter(i -> WildcardMatcher.match(resourceCode, i)).forEach(r -> {
                    result.add(new PermissionInstance(r, actionCode, orgId));
                });
            } else {
                result.add(new PermissionInstance(resourceCode, actionCode, orgId));
            }
        });
        return result;
    }
}
