package com.yunsoo.api.rabbit.security.permission;

import com.yunsoo.api.rabbit.dto.User;
import com.yunsoo.api.rabbit.dto.basic.UserLikedProduct;
import com.yunsoo.api.rabbit.dto.basic.UserOrganizationFollowing;
import com.yunsoo.api.rabbit.dto.basic.UserProductFollowing;
import com.yunsoo.api.rabbit.object.TUser;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

/**
 * Created by:   Zhe
 * Created on:   2015/3/6
 * Descriptions:
 */
public class BasePermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;
        if (authentication != null && permission instanceof String) {

            TUser tUser = (TUser) SecurityContextHolder.getContext().getAuthentication().getDetails();


            if (targetDomainObject instanceof User) {
                hasPermission = ((User) targetDomainObject).getId().equals(tUser.getId());
            } else if (targetDomainObject instanceof UserLikedProduct) {
                hasPermission = ((UserLikedProduct) targetDomainObject).getUserId().equals(tUser.getId());
            } else if (targetDomainObject instanceof UserOrganizationFollowing) {
                hasPermission = ((UserOrganizationFollowing) targetDomainObject).getUserId().equals(tUser.getId());
            } else if (targetDomainObject instanceof UserProductFollowing) {
                hasPermission = ((UserProductFollowing) targetDomainObject).getUserId().equals(tUser.getId());
            }else {
                hasPermission = true;
            }
            //mockup here, always true
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

            TUser tUser = (TUser) SecurityContextHolder.getContext().getAuthentication().getDetails();

            //check if user's permission for target id
            if (targetType.compareToIgnoreCase("User") == 0) {
                hasPermission = tUser.getId().equals(targetId);
            } else if (targetType.compareToIgnoreCase("UserLikedProduct") == 0) {
                hasPermission = tUser.getId().equals(targetId);
            } else if (targetType.compareToIgnoreCase("UserFollowing") == 0) {
                hasPermission = tUser.getId().equals(targetId);
            } else if (targetType.compareToIgnoreCase("UserInToken") == 0) {
                hasPermission = tUser.getId().equals(targetId);
            } else {
                hasPermission = false;
            }

        }
        return hasPermission;
    }
}
