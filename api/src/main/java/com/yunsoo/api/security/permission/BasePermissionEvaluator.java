package com.yunsoo.api.security.permission;

import com.yunsoo.api.domain.PermissionDomain;
import com.yunsoo.api.dto.basic.Message;
import com.yunsoo.api.dto.basic.User;
import com.yunsoo.api.object.TAccount;
import com.yunsoo.api.object.TPermission;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.ForbiddenException;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.apache.jasper.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zhe on 2015/3/6.
 */
public class BasePermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private PermissionDomain permissionDomain;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;
        if (authentication != null && permission instanceof String) {
            TAccount account = (TAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();
            if (!account.isAnonymous()) {
                throw new ForbiddenException(40301, "Action-" + permission, "Anonymous user is denied!");
            } else if (!account.isAccountNonExpired()) {
                throw new UnauthorizedException(40101, "Account is expired");
            } else if (!account.isAccountNonLocked()) {
                throw new UnauthorizedException(40102, "Account is locked!");
            } else if (account.isCredentialsInvalid()) {
                throw new UnauthorizedException(40103, "Account token is invalid!");
            } else if (!account.isEnabled()) {
                throw new UnauthorizedException(40104, "Account is disabled!");
            }

            List<TPermission> permissionList = permissionDomain.getAccountPermissionsByAccountId(account.getId());

            TPermission currentPermission = new TPermission();
            currentPermission.setOrgId(account.getOrgId());
            hasPermission = permissionDomain.hasPermission(account.getId(), currentPermission);
            //implement the permission checking of your application here
            //you can just check if the input permission is within your permission list

            //In my example, the user object contains a HashMap which stored the permission of the user.
            //The HashMap<String, PrivilegeResult> is populated during using login by filter. This will not be shown in this example

//            AccountPermissionObject[] permissionObjectList =  dataAPIClient.get("accountpermission/permission/{accountId}", AccountPermissionObject[].class, account.getId());
//            User user = SecurityUtil.getUserCredential();
//            HashMap<String, PrivilegeResult> pMap =user.getPrivilegeMap();
//            PrivilegeResult privResult = pMap.get(permission);
//            hasPermission =  privResult.isAllowAccess();
        }
        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId, String targetType, Object permission) {
        boolean hasPermission = false;
        if (authentication != null && permission instanceof String) {

            TAccount account = (TAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();
            if (!account.isAnonymous()) {
                throw new ForbiddenException(40301, "Action-" + permission, "Anonymous user is denied!");
            } else if (!account.isAccountNonExpired()) {
                throw new UnauthorizedException(40101, "Account is expired");
            } else if (!account.isAccountNonLocked()) {
                throw new UnauthorizedException(40102, "Account is locked!");
            } else if (account.isCredentialsInvalid()) {
                throw new UnauthorizedException(40103, "Account token is invalid!");
            } else if (!account.isEnabled()) {
                throw new UnauthorizedException(40104, "Account is disabled!");
            }

            //check if user's permission for target id
//            if (targetType.compareToIgnoreCase("User") == 0) {
//                hasPermission = account.getId().equals(targetId) ? true : false;
//            } else if (targetType.compareToIgnoreCase("UserLikedProduct") == 0) {
//                hasPermission = account.getId().equals(targetId) ? true : false;
//            } else if (targetType.compareToIgnoreCase("AccountInToken") == 0) {
//                hasPermission = account.getId().equals(targetId) ? true : false;
//            } else {
//                hasPermission = false;
//            }

            List<TPermission> permissionList = permissionDomain.getAccountPermissionsByAccountId(account.getId());

            String[] pArray = ((String) permission).toLowerCase().split(":");
            TPermission currentPermission = new TPermission();
            currentPermission.setOrgId(account.getOrgId());
            currentPermission.setResourceCode(pArray[0]);
            currentPermission.setActionCode(pArray[1]);
            hasPermission = permissionDomain.hasPermission(account.getId(), currentPermission);

        }
        return hasPermission;
    }
}