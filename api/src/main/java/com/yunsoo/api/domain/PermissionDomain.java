package com.yunsoo.api.domain;

import com.yunsoo.api.dto.PermissionAction;
import com.yunsoo.api.dto.PermissionResource;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
@Component
public class PermissionDomain {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionDomain.class);

    @Autowired
    private RestClient dataAPIClient;

    @Cacheable(value = "permission", key = "'policylist'")
    public List<PermissionPolicyObject> getPermissionPolicies() {
        //LOGGER.debug("cache missed [name: permission, key: 'policylist']");
        return dataAPIClient.get("permission/policy", new ParameterizedTypeReference<List<PermissionPolicyObject>>() {
        });
    }

    @Cacheable(value = "permission", key = "'policymap'")
    public Map<String, PermissionPolicyObject> getPermissionPolicyMap() {
        //LOGGER.debug("cache missed [name: permission, key: 'policymap']");
        Map<String, PermissionPolicyObject> permissionPolicies = new HashMap<>();
        this.getPermissionPolicies().forEach(o -> {
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

    //PermissionResource
    public List<PermissionResource> getPermissionResources() {
        return getPermissionResources(null);
    }

    public List<PermissionResource> getPermissionResources(Boolean active) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("active", active).build();
        return dataAPIClient.get("permission/resource" + query, new ParameterizedTypeReference<List<PermissionResource>>() {
        });
    }

    //PermissionAction
    public List<PermissionAction> getPermissionActions() {
        return getPermissionActions(null);
    }

    public List<PermissionAction> getPermissionActions(Boolean active) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("active", active).build();
        return dataAPIClient.get("permission/action" + query, new ParameterizedTypeReference<List<PermissionAction>>() {
        });
    }


}
