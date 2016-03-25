package com.yunsoo.api.security.permission.expression;

/**
 * Created by:   Lijian
 * Created on:   2016-03-21
 * Descriptions:
 */
public abstract class PermissionExpression extends ResourceExpression {

    public PermissionExpression(String expressionOrValue) {
        super(expressionOrValue);
    }

    public static PermissionExpression newInstance(String expression) {
        if (expression == null) {
            return null;
        } else if (expression.startsWith(PolicyPermissionExpression.PREFIX)) {
            return new PolicyPermissionExpression(expression);
        } else if (expression.contains(SimplePermissionExpression.SP)) {
            return new SimplePermissionExpression(expression);
        } else {
            return null;
        }
    }

    public static class PolicyPermissionExpression extends PermissionExpression {

        private static final String RESOURCE = "policy";
        private static final String PREFIX = RESOURCE + "/";

        public PolicyPermissionExpression(String expressionOrPolicyCode) {
            super(expressionOrPolicyCode);
            setResource(RESOURCE);
        }

        public String getPolicyCode() {
            return getValue();
        }
    }

    public static class SimplePermissionExpression extends PermissionExpression {

        private static final String RESOURCE = null;
        private static final String SP = ":";

        private String resourceCode;

        private String actionCode;

        public SimplePermissionExpression(String expression) {
            super(expression);
            setResource(RESOURCE);
            String[] tempArray = expression.split(SP, 2);
            this.resourceCode = tempArray[0];
            this.actionCode = tempArray[1];
        }

        public SimplePermissionExpression(String resourceCode, String actionCode) {
            super(String.format("%s%s%s", resourceCode, SP, actionCode));
            setResource(RESOURCE);
            this.resourceCode = resourceCode;
            this.actionCode = actionCode;
        }

        public String getResourceCode() {
            return resourceCode;
        }

        public String getActionCode() {
            return actionCode;
        }
    }

}