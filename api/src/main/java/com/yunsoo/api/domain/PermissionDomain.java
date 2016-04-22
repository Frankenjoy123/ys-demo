package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
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

import java.util.List;

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

    //@Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'region/' + #id)")
    public PermissionRegionObject getPermissionRegionById(String id) {
        if (id == null || id.length() == 0) return null;
        try {
            return dataAPIClient.get("permission/region/{id}", PermissionRegionObject.class, id);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    /**
     * put the orgRestriction(orgId) to the restrictions of default permission region of the masterOrgId
     *
     * @param masterOrgId    current org id
     * @param orgRestriction sub org id
     */
    public void putOrgRestrictionToDefaultPermissionRegion(String masterOrgId, String orgRestriction) {
        orgRestriction = new RestrictionExpression.OrgRestrictionExpression(orgRestriction).toString();
        PermissionRegionObject defaultPR = getOrCreateDefaultPermissionRegion(masterOrgId);

        defaultPR.getRestrictions().add(orgRestriction);
        dataAPIClient.patch("permission/region/{id}", defaultPR, defaultPR.getId());
    }

    public PermissionRegionObject getOrCreateDefaultPermissionRegion(String orgId) {
        PermissionRegionObject defaultPR;
        List<PermissionRegionObject> defaultRegionObjects = dataAPIClient.get("permission/region?org_id={orgId}&type_code={typeCode}", new ParameterizedTypeReference<List<PermissionRegionObject>>() {
        }, orgId, LookupCodes.PermissionRegionType.DEFAULT);

        if (defaultRegionObjects.size() == 0) {
            //create new default region
            defaultPR = new PermissionRegionObject();
            defaultPR.setOrgId(orgId);
            defaultPR.setTypeCode(LookupCodes.PermissionRegionType.DEFAULT);
            defaultPR.setName("Default Region");
            defaultPR = dataAPIClient.post("permission/region", defaultPR, PermissionRegionObject.class);
        } else if (defaultRegionObjects.size() == 1) {
            defaultPR = defaultRegionObjects.get(0);
        } else {
            defaultPR = defaultRegionObjects.get(0);
            //merge other default regions if exist
            for (int i = 1; i < defaultRegionObjects.size(); i++) {
                defaultPR.getRestrictions().addAll(defaultRegionObjects.get(i).getRestrictions());
            }
            dataAPIClient.patch("permission/region/{id}", defaultPR, defaultPR.getId());
            for (int i = 1; i < defaultRegionObjects.size(); i++) {
                dataAPIClient.delete("permission/region/{id}", defaultRegionObjects.get(i).getId());
            }
        }
        return defaultPR;
    }

    //endregion

    //region policy

    @Cacheable(key = "T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PERMISSION.toString(), 'policyList')")
    public List<PermissionPolicyObject> getPermissionPolicies() {
        return dataAPIClient.get("permission/policy", new ParameterizedTypeReference<List<PermissionPolicyObject>>() {
        });
    }

    //endregion

}
