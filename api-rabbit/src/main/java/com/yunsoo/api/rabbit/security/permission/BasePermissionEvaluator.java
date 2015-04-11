package com.yunsoo.api.rabbit.security.permission;

import com.yunsoo.api.rabbit.object.TAccount;
import com.yunsoo.common.web.exception.UnauthorizedException;
import org.apache.jasper.security.SecurityUtil;
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

            //implement the permission checking of your application here
            //you can just check if the input permission is within your permission list
            TAccount account = (TAccount) SecurityContextHolder.getContext().getAuthentication().getDetails();
            hasPermission = true; //mockup here, always true

            if (!account.isAccountNonExpired()) {
                throw new UnauthorizedException(40102, "Account token is expired");
//                hasPermission = false;
            }
            if (!account.isDefined()) {
                throw new UnauthorizedException(40101, "Account token is invalid!");
//                hasPermission = false;
            }
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
