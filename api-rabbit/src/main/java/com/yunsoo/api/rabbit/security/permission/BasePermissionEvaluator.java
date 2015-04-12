package com.yunsoo.api.rabbit.security.permission;

import com.yunsoo.api.rabbit.object.TAccount;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

/**
 * Created by Zhe on 2015/3/6.
 */
public class BasePermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;
        if (authentication != null && permission instanceof String) {

            TAccount account = (TAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();
            if (!account.isAnonymous()) {
                throw new UnauthorizedException(40101, "Anonymous user is denied!");
            } else if (!account.isAccountNonExpired()) {
                throw new UnauthorizedException(40102, "Account is expired");
            } else if (!account.isAccountNonLocked()) {
                throw new UnauthorizedException(40103, "Account is locked!");
            } else if (account.isCredentialsInvalid()) {
                throw new UnauthorizedException(40104, "Account token is invalid!");
            } else if (!account.isEnabled()) {
                throw new UnauthorizedException(40105, "Account is disabled!");
            }
            hasPermission = true; //mockup here, always true
            //implement the permission checking of your application here
            //you can just check if the input permission is within your permission list

            //In my example, the user object contains a HashMap which stored the permission of the user.
            //The HashMap<String, PrivilegeResult> is populated during using login by filter. This will not be shown in this example

//            User user = SecurityUtil.getUserCredential();
//            HashMap<String, PrivilegeResult> pMap =user.getPrivilegeMap();
//            PrivilegeResult privResult = pMap.get(permission);
//            hasPermission =  privResult.isAllowAccess();
        } else {
            hasPermission = false;
        }
        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId, String targetType, Object permission) {
        throw new RuntimeException("Id and Class permissions are not supperted by this application");
    }
}