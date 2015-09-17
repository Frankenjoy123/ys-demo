package com.yunsoo.api.rabbit.security.permission;

import com.yunsoo.api.rabbit.dto.User;
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
            }
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
            if (targetType.compareToIgnoreCase("userId") == 0) {
                hasPermission = "current".equals(targetId) || tUser.getId().equals(targetId);
            }
        }
        return hasPermission;
    }
}
