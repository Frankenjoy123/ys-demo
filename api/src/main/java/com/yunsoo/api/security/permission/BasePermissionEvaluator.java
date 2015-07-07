package com.yunsoo.api.security.permission;

import com.yunsoo.api.domain.AccountPermissionDomain;
import com.yunsoo.api.dto.*;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.common.data.object.LogisticsCheckActionObject;
import com.yunsoo.common.data.object.LogisticsCheckPointObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/6
 * Descriptions:
 */
public class BasePermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private AccountPermissionDomain accountPermissionDomain;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;
        if (authentication != null && permission instanceof String) {
            TAccount account = (TAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();

            TPermission currentPermission = this.getPermission((String) permission);
            if (targetDomainObject instanceof Message) {
                currentPermission.setOrgId(((Message) targetDomainObject).getOrgId());
            } else if (targetDomainObject instanceof Organization) {
                currentPermission.setOrgId(((Organization) targetDomainObject).getId());
            } else if (targetDomainObject instanceof ProductBase) {
                currentPermission.setOrgId(((ProductBase) targetDomainObject).getOrgId());
            } else if (targetDomainObject instanceof LogisticsCheckActionObject) {
                currentPermission.setOrgId(((LogisticsCheckActionObject) targetDomainObject).getOrgId());
            } else if (targetDomainObject instanceof LogisticsCheckPointObject) {
                currentPermission.setOrgId(((LogisticsCheckPointObject) targetDomainObject).getOrgId());
            } else if (targetDomainObject instanceof Device) {
                currentPermission.setOrgId(((Device) targetDomainObject).getOrgId());
            } else if (targetDomainObject instanceof ProductKeyOrder) {
                currentPermission.setOrgId(((ProductKeyOrder) targetDomainObject).getOrgId());
            } else if (targetDomainObject instanceof Group) {
                currentPermission.setOrgId(((Group) targetDomainObject).getOrgId());
            }

            if (StringUtils.isEmpty(currentPermission.getOrgId())) {
                currentPermission.setOrgId(account.getOrgId());  //by default, just set the account's orgId
            }

            hasPermission = accountPermissionDomain.hasPermission(account.getId(), currentPermission);
        }
        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId, String targetType, Object permission) {
        boolean hasPermission = false;
        if (authentication != null && permission instanceof String) {

            TAccount account = (TAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();

            TPermission currentPermission = this.getPermission((String) permission);
            //check if user trying to take action on it's own orgId's resource
            if (targetType.compareToIgnoreCase("filterByOrg") == 0) {
//                hasPermission = account.getOrgId().equals(targetId) ? true : false;
                String orgId = targetId == null ? null : targetId.toString();
                currentPermission.setOrgId(StringUtils.hasText(orgId) ? orgId : account.getOrgId());
            }
//            else if (targetType.compareToIgnoreCase("UserLikedProduct") == 0) {
//                hasPermission = account.getId().equals(targetId) ? true : false;
//            } else if (targetType.compareToIgnoreCase("AccountInToken") == 0) {
//                hasPermission = account.getId().equals(targetId) ? true : false;
//            } else {
//                hasPermission = false;
//            }
            else {
                currentPermission.setOrgId(account.getOrgId());
            }
            hasPermission = accountPermissionDomain.hasPermission(account.getId(), currentPermission);
        }
        return hasPermission;
    }

    private TPermission getPermission(String permission) {
        String[] pArray = permission.toLowerCase().split(":");
        if (pArray.length != 2) {
            throw new RuntimeException("incorrect permission setting {" + permission + "}");
        }
        TPermission currentPermission = new TPermission();
        currentPermission.setResourceCode(pArray[0]);
        currentPermission.setActionCode(pArray[1]);
        return currentPermission;
    }

}