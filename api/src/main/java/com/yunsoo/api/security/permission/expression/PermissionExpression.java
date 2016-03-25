package com.yunsoo.api.security.permission.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-03-21
 * Descriptions: key characters: [/,*:]
 */
public abstract class PermissionExpression extends ResourceExpression {

    public PermissionExpression(String expressionOrValue) {
        super(expressionOrValue);
    }

    public static PermissionExpression parse(String expression) {
        if (expression == null) {
            return null;
        } else if (expression.contains(COLLECTION_DELIMITER)) {
            return new CollectionPermissionExpression(expression);
        } else if (expression.startsWith(PolicyPermissionExpression.PREFIX)) {
            return new PolicyPermissionExpression(expression);
        } else if (!expression.contains(DELIMITER) && expression.contains(SimplePermissionExpression.OPERATOR)) {
            return new SimplePermissionExpression(expression);
        } else {
            return null;
        }
    }

    public static <E extends PermissionExpression> PermissionExpression collect(List<E> expressions) {
        return expressions == null || expressions.size() == 0 ? null
                : expressions.size() == 1 ? expressions.get(0)
                : new CollectionPermissionExpression(expressions);
    }


    public static class PolicyPermissionExpression extends PermissionExpression {

        private static final String RESOURCE = "policy";
        private static final String PREFIX = RESOURCE + DELIMITER;

        public PolicyPermissionExpression(String expressionOrPolicyCode) {
            super(expressionOrPolicyCode);
            setResource(RESOURCE);
        }

        public String getPolicyCode() {
            return getValue();
        }
    }

    public static class SimplePermissionExpression extends PermissionExpression {

        private static final String OPERATOR = ":";

        private String resourceCode;

        private String actionCode;

        public SimplePermissionExpression(String expression) {
            super(expression);
            setResource(null);
            String[] tempArray = expression.split(OPERATOR, 2);
            this.resourceCode = tempArray[0];
            this.actionCode = tempArray[1];
        }

        public SimplePermissionExpression(String resourceCode, String actionCode) {
            super(String.format("%s%s%s", resourceCode, OPERATOR, actionCode));
            setResource(null);
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

    public static class CollectionPermissionExpression extends PermissionExpression {

        private List<PermissionExpression> expressions;

        public List<PermissionExpression> getExpressions() {
            return expressions;
        }

        public CollectionPermissionExpression(String expression) {
            super(expression);
            setResource(COLLECTION_RESOURCE);
            this.expressions = new ArrayList<>();
            for (String e : expression.split(COLLECTION_DELIMITER)) {
                PermissionExpression exp = parse(e);
                if (exp != null) this.expressions.add(exp);
            }
        }

        public <E extends PermissionExpression> CollectionPermissionExpression(List<E> expressions) {
            super(ResourceExpression.toString(expressions));
            setResource(COLLECTION_RESOURCE);
            if (expressions != null) {
                this.expressions = expressions.stream().collect(Collectors.toList());
            } else {
                this.expressions = new ArrayList<>();
            }
        }
    }

}
