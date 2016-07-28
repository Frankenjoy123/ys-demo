package com.yunsoo.common.web.security.permission;

import com.yunsoo.common.web.security.permission.expression.PermissionExpression;
import com.yunsoo.common.web.security.permission.expression.PrincipalExpression;
import com.yunsoo.common.web.security.permission.expression.ResourceExpression;
import com.yunsoo.common.web.security.permission.expression.RestrictionExpression;

import java.io.Serializable;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions: corresponding to the permission_allocation by one to one
 */
public class PermissionEntry implements Comparable, Serializable {

    private String id; //the same as permission_allocation.id

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

    public boolean isValid() {
        return id != null && principal != null && restriction != null && permission != null && effect != null;
    }

    public PermissionEntry.Effect check(RestrictionExpression restriction, PermissionExpression permission) {
        if (!isValid() || restriction == null || permission == null) {
            return null;
        }
        if (this.restriction.contains(restriction)
                && this.permission.contains(permission)) {
            return this.effect;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionEntry that = (PermissionEntry) o;

        return ResourceExpression.equals(principal, that.principal)
                && ResourceExpression.equals(restriction, that.restriction)
                && ResourceExpression.equals(permission, that.permission)
                && (effect == that.effect || effect != null && effect.equals(that.effect));
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

    public static PermissionEntry parse(String str) {
        if (str == null || str.length() < 58) { //[id: , principal: , restriction: , permission: , effect: ]
            return null;
        }

        String raw = str.substring(1, str.length() - 1);
        String[] items = raw.split("[,:] ");

        if (items.length != 10) {
            return null;
        }

        String id = items[1].equals("null") ? null : items[1];
        String principal = items[3].equals("null") ? null : items[3];
        String restriction = items[5].equals("null") ? null : items[5];
        String permission = items[7].equals("null") ? null : items[7];
        String effect = items[9].equals("null") ? null : items[9];

        PermissionEntry permissionEntry = new PermissionEntry();
        permissionEntry.setId(id);
        permissionEntry.setPrincipal(PrincipalExpression.parse(principal));
        permissionEntry.setRestriction(RestrictionExpression.parse(restriction));
        permissionEntry.setPermission(PermissionExpression.parse(permission));
        permissionEntry.setEffect(Effect.parse(effect));

        return permissionEntry;
    }


    public enum Effect {
        allow,
        deny;

        public static Effect parse(String name) {
            if (name == null) {
                return null;
            }
            if (name.equals(allow.name())) {
                return allow;
            }
            if (name.equals(deny.name())) {
                return deny;
            } else {
                return null;
            }
        }
    }

}
