package com.yunsoo.api.security.permission;

import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.PrincipalExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions:
 */
public class Permission {

    private PrincipalExpression principal;

    private RestrictionExpression restriction;

    private PermissionExpression permission;

    private Effect effect;

    public PrincipalExpression getPrincipal() {
        return principal;
    }

    public void setPrincipal(PrincipalExpression principal) {
        this.principal = principal;
    }

    public RestrictionExpression getRestriction() {
        return restriction;
    }

    public void setRestriction(RestrictionExpression restriction) {
        this.restriction = restriction;
    }

    public PermissionExpression getPermission() {
        return permission;
    }

    public void setPermission(PermissionExpression permission) {
        this.permission = permission;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public static enum Effect {
        allow,
        deny
    }
}
