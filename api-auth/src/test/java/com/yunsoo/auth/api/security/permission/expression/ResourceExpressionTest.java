package com.yunsoo.auth.api.security.permission.expression;

import org.junit.Test;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions:
 */
public class ResourceExpressionTest {

    @Test
    public void test_equals() {
        PermissionExpression pe1 = PermissionExpression.parse("account:read");
        PermissionExpression pe2 = PermissionExpression.parse("account:read");
        assert pe1.equals(pe2);

        PermissionExpression pe3 = PermissionExpression.parse("policy/admin");
        assert !pe1.equals(pe3);

        assert pe3.compareTo(null) == -1;
        assert pe1.compareTo(pe2) == 0;
        try {
            pe1.compareTo(pe3);
            assert false;
        } catch (ClassCastException ignored) {
            assert true;
        }

        assert ResourceExpression.equals(pe1, pe2);
        assert ResourceExpression.equals(null, null);
        assert !ResourceExpression.equals(null, pe2);
        assert !ResourceExpression.equals(pe1, new PermissionExpression.CollectionPermissionExpression("account:read"));
    }

    @Test
    public void test_constructor() {
        PrincipalExpression pe1 = PrincipalExpression.parse("account/12345");
        PrincipalExpression pe2 = new PrincipalExpression.AccountPrincipalExpression("12345");
        assert pe1.equals(pe2);

        PermissionExpression pe3 = PermissionExpression.parse("account:read,policy/admin");
        assert pe3 instanceof PermissionExpression.CollectionPermissionExpression;
        assert ((PermissionExpression.CollectionPermissionExpression) pe3).getExpressions().size() == 2;

    }

    @Test
    public void test_RestrictionExpression_contains() {
        RestrictionExpression exp0 = new RestrictionExpression.OrgRestrictionExpression("12345");
        RestrictionExpression exp1 = new RestrictionExpression.OrgRestrictionExpression("12345");
        RestrictionExpression exp2 = new RestrictionExpression.OrgRestrictionExpression("*");
        RestrictionExpression exp3 = new RestrictionExpression.CollectionRestrictionExpression("org/12345,org/45678");

        assert exp1.contains(exp0);
        assert exp2.contains(exp0);
        assert exp3.contains(exp0);
    }

    @Test
    public void test_PermissionExpression_contains() {
        PermissionExpression exp0 = new PermissionExpression.SimplePermissionExpression("account:read");
        PermissionExpression exp1 = new PermissionExpression.SimplePermissionExpression("account:read");
        PermissionExpression exp2 = new PermissionExpression.SimplePermissionExpression("account:*");
        PermissionExpression exp3 = new PermissionExpression.SimplePermissionExpression("*:read");
        PermissionExpression exp4 = new PermissionExpression.CollectionPermissionExpression("*:*,account:delete");

        assert exp1.contains(exp0);
        assert exp2.contains(exp0);
        assert exp3.contains(exp0);
        assert exp4.contains(exp0);

        PermissionExpression exp00 = new PermissionExpression.SimplePermissionExpression("account", null);

        assert exp1.contains(exp00);
        assert exp2.contains(exp00);
        assert exp3.contains(exp00);
        assert exp4.contains(exp00);

        PermissionExpression exp000 = new PermissionExpression.SimplePermissionExpression(null, null);

        assert !exp1.contains(exp000);
        assert !exp2.contains(exp000);
        assert !exp3.contains(exp000);
        assert !exp4.contains(exp000);

    }
}
