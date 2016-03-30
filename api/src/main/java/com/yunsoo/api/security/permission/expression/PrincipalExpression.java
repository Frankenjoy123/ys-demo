package com.yunsoo.api.security.permission.expression;

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

        private static final String RESOURCE = "account";
        private static final String PREFIX = RESOURCE + DELIMITER;

        public static final AccountPrincipalExpression ANY = new AccountPrincipalExpression("*");

        public AccountPrincipalExpression(String expressionOrAccountId) {
            super(expressionOrAccountId);
            setResource(RESOURCE);
        }

        public String getAccountId() {
            return getValue();
        }

    }

    public static class GroupPrincipalExpression extends PrincipalExpression {

        private static final String RESOURCE = "group";
        private static final String PREFIX = RESOURCE + DELIMITER;

        public GroupPrincipalExpression(String expressionOrGroupId) {
            super(expressionOrGroupId);
            setResource(RESOURCE);
        }

        public String getGroupId() {
            return getValue();
        }

    }

}
