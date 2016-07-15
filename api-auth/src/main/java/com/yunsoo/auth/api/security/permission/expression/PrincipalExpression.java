package com.yunsoo.auth.api.security.permission.expression;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions: key characters: [/,]
 */
public abstract class PrincipalExpression extends ResourceExpression {

    public PrincipalExpression(String expressionOrValue) {
        super(expressionOrValue);
    }

    public static PrincipalExpression parse(String expression) {
        if (expression == null) {
            return null;
        } else if (expression.startsWith(AccountPrincipalExpression.PREFIX)) {
            return new AccountPrincipalExpression(expression);
        } else if (expression.startsWith(GroupPrincipalExpression.PREFIX)) {
            return new GroupPrincipalExpression(expression);
        }
        return null;
    }


    public static class AccountPrincipalExpression extends PrincipalExpression {

        private static final String TYPE = "account";
        private static final String PREFIX = TYPE + DELIMITER;

        public static final AccountPrincipalExpression ANY = new AccountPrincipalExpression("*");

        public AccountPrincipalExpression(String expressionOrAccountId) {
            super(expressionOrAccountId);
            setType(TYPE);
        }

        public String getAccountId() {
            return getValue();
        }

    }

    public static class GroupPrincipalExpression extends PrincipalExpression {

        private static final String TYPE = "group";
        private static final String PREFIX = TYPE + DELIMITER;

        public GroupPrincipalExpression(String expressionOrGroupId) {
            super(expressionOrGroupId);
            setType(TYPE);
        }

        public String getGroupId() {
            return getValue();
        }

    }

}
