package com.yunsoo.api.security.permission.expression;

import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions:
 */
public class ResourceExpressionTest {

    @Test
    public void test_equals() {
        PermissionExpression pe1 = PermissionExpression.newInstance("account:read");
        PermissionExpression pe2 = PermissionExpression.newInstance("account:read");
        assert pe1.equals(pe2);

        PermissionExpression pe3 = PermissionExpression.newInstance("policy/admin");
        assert !pe1.equals(pe3);

        assert pe3.compareTo(null) == -1;
        assert pe1.compareTo(pe2) == 0;
        try {
            pe1.compareTo(pe3);
            assert false;
        } catch (ClassCastException ignored) {
            assert true;
        }
    }

    @Test
    public void test_constructor() {
        PrincipalExpression pe1 = PrincipalExpression.newInstance("account/12345");
        PrincipalExpression pe2 = new PrincipalExpression.AccountPrincipalExpression("12345");
        assert pe1.equals(pe2);


    }
}
