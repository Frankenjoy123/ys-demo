package com.yunsoo.api.domain;

import com.yunsoo.api.dto.PermissionAction;
import com.yunsoo.api.dto.PermissionResource;
import com.yunsoo.common.data.object.PermissionPolicyObject;
import com.yunsoo.common.web.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


    public Map<String, PermissionPolicyObject> getPermissionPolicyMap() {
        Map<String, PermissionPolicyObject> permissionPolicies = new HashMap<>();
        List<PermissionPolicyObject> permissionPolicyObjects = dataAPIClient.get("permission/policy",
                new ParameterizedTypeReference<List<PermissionPolicyObject>>() {
                });
        permissionPolicyObjects.forEach(o -> {
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

    public List<PermissionResource> getAllActivePermissionResources() {
        return dataAPIClient.get("permission/resource", new ParameterizedTypeReference<List<PermissionResource>>() {
        });
    }

    public List<PermissionAction> getAllActivePermissionActions() {
        return dataAPIClient.get("permission/action", new ParameterizedTypeReference<List<PermissionAction>>() {
        });
    }


}
