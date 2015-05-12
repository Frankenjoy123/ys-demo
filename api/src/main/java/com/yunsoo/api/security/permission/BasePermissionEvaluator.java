package com.yunsoo.api.security.permission;

import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.dto.ProductKeyOrder;
import com.yunsoo.api.dto.basic.Device;
import com.yunsoo.api.dto.basic.Message;
import com.yunsoo.api.dto.basic.Organization;
import com.yunsoo.api.dto.basic.ProductBase;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.common.data.object.LogisticsCheckActionObject;
import com.yunsoo.common.data.object.LogisticsCheckPointObject;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

/**
 * Created by  : Zhe
 * Created on  : 2015/3/6
 * Descriptions:
 */
public class BasePermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private PermissionDomain permissionDomain;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;
        if (authentication != null && permission instanceof String) {
            TAccount account = (TAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();
            this.checkAccount(account);

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
            }

            if (currentPermission.getOrgId() == null || currentPermission.getOrgId().trim().isEmpty()) {
                currentPermission.setOrgId(account.getOrgId());  //by default, just set the account's orgId
            }

            hasPermission = permissionDomain.hasPermission(account.getId(), currentPermission);
        }
        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId, String targetType, Object permission) {
        boolean hasPermission = false;
        if (authentication != null && permission instanceof String) {

            TAccount account = (TAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();
            this.checkAccount(account);

            TPermission currentPermission = this.getPermission((String) permission);
            //check if user trying to take action on it's own orgId's resource
            if (targetType.compareToIgnoreCase("filterByOrg") == 0) {
//                hasPermission = account.getOrgId().equals(targetId) ? true : false;
                currentPermission.setOrgId(targetId.toString());
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
            hasPermission = permissionDomain.hasPermission(account.getId(), currentPermission);
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

    private void checkAccount(TAccount account) {
        if (!account.isAnonymous()) {
            throw new ForbiddenException(40301, "Action", "Anonymous user is denied!");
        } else if (!account.isAccountNonExpired()) {
            throw new UnauthorizedException(40101, "Account is expired");
        } else if (!account.isAccountNonLocked()) {
            throw new UnauthorizedException(40102, "Account is locked!");
        } else if (account.isCredentialsInvalid()) {
            throw new UnauthorizedException(40103, "Account token is invalid!");
        } else if (account.isTokenExpired()) {
            throw new UnauthorizedException(40104, "Account token is expired!");
        } else if (!account.isEnabled()) {
            throw new UnauthorizedException(40105, "Account is disabled!");
        }
    }
}