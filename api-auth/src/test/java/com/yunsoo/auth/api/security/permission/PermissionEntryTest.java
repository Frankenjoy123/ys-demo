package com.yunsoo.auth.api.security.permission;

import com.yunsoo.auth.api.security.permission.expression.PermissionExpression;
import com.yunsoo.auth.api.security.permission.expression.PrincipalExpression;
import com.yunsoo.auth.api.security.permission.expression.RestrictionExpression;
import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2016-03-24
 * Descriptions:
 */
public class PermissionEntryTest {

    @Test
    public void test_equals() {
        PermissionEntry pe1 = new PermissionEntry();
        PermissionEntry pe2 = new PermissionEntry();

        pe1.setId("2lvt1gk4upnbfsglnrk");
        pe2.setId("2lvt1gk4upnbfsglnrk");
        assert pe1.equals(pe2);

        pe1.setPrincipal(PrincipalExpression.parse("account/12345"));
        assert !pe1.equals(pe2);

        pe2.setPrincipal(PrincipalExpression.parse("account/12345"));
        assert pe1.equals(pe2);
    }

    @Test
    public void test_serialization() {
        PermissionEntry pe1 = new PermissionEntry();
        pe1.setEffect(null);

        PermissionEntry pe2 = PermissionEntry.parse(pe1.toString());
        assert pe2.getId() == null;
        assert pe2.getPrincipal() == null;
        assert pe2.getRestriction() == null;
        assert pe2.getPermission() == null;
        assert pe2.getEffect() == null;


        pe1.setId("2lvt1gk4upnbfsglnrk");
        pe1.setPrincipal(PrincipalExpression.parse("account/12345"));
        pe1.setRestriction(RestrictionExpression.parse("org/12345,org/67890"));
        pe1.setPermission(PermissionExpression.parse("account:read,account:create"));
        pe1.setEffect(PermissionEntry.Effect.allow);

        pe2 = PermissionEntry.parse(pe1.toString());

        assert pe1.equals(pe2);
        assert pe1.toString().equals(pe2.toString());

        System.out.println(pe1);
        System.out.println(pe2);
    }

}
