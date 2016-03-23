package com.yunsoo.api.security.permission;

import com.yunsoo.api.security.permission.expression.PermissionExpression;
import com.yunsoo.api.security.permission.expression.PrincipalExpression;
import com.yunsoo.api.security.permission.expression.RestrictionExpression;
import com.yunsoo.common.data.object.PermissionAllocationObject;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions:
 */
public class PermissionEntry implements Comparable {

    private String id; //permission_allocation.id

    private PrincipalExpression principal;

    private RestrictionExpression restriction;

    private PermissionExpression permission;

    private Effect effect = Effect.allow; //default allow


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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


    public PermissionEntry() {
    }

    public PermissionEntry(PermissionAllocationObject paObject) {
        this.id = paObject.getId();
        this.principal = PrincipalExpression.newInstance(paObject.getPrincipal());
        this.restriction = RestrictionExpression.newInstance(paObject.getRestriction());
        this.permission = PermissionExpression.newInstance(paObject.getPermission());
        this.effect = Effect.valueOf(paObject.getEffect().name());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionEntry that = (PermissionEntry) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return -1;
        if (getClass() != o.getClass()) throw new ClassCastException();

        PermissionEntry that = (PermissionEntry) o;

        if (effect.equals(that.getEffect())) return this.id.compareTo(that.getId());

        return effect == Effect.deny ? -1 : 1;
    }

    @Override
    public String toString() {
        return String.format("[id: %s, principal: %s, restriction: %s, permission: %s, effect: %s]",
                id, principal, restriction, permission, effect);
    }


    public static enum Effect {
        allow,
        deny
    }
}
