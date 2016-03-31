package com.yunsoo.api.security.permission.expression;

import com.yunsoo.api.util.WildcardMatcher;

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

    public abstract boolean contains(PermissionExpression permission);

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


    public static class SimplePermissionExpression extends PermissionExpression {

        private static final String OPERATOR = ":";

        private String resourceCode;

        private String actionCode;

        public SimplePermissionExpression(String expression) {
            super(expression);
            setType(null);
            String[] tempArray = expression.split(OPERATOR, 2);
            this.resourceCode = tempArray[0];
            this.actionCode = tempArray[1];
        }

        public SimplePermissionExpression(String resourceCode, String actionCode) {
            super(String.format("%s%s%s", resourceCode, OPERATOR, actionCode));
            setType(null);
            this.resourceCode = resourceCode;
            this.actionCode = actionCode;
        }

        public String getResourceCode() {
            return resourceCode;
        }

        public String getActionCode() {
            return actionCode;
        }

        @Override
        public boolean contains(PermissionExpression permission) {
            if (resourceCode == null || actionCode == null || permission == null
                    || !(permission instanceof SimplePermissionExpression)) {
                return false;
            }
            SimplePermissionExpression simplePermission = (SimplePermissionExpression) permission;
            boolean resourceIsMatch = WildcardMatcher.match(resourceCode, simplePermission.getResourceCode());
            boolean actionIsMatch = actionCode.equals("*") || actionCode.equals(simplePermission.getActionCode());
            return resourceIsMatch && actionIsMatch;
        }
    }

    public static class PolicyPermissionExpression extends PermissionExpression {

        private static final String TYPE = "policy";
        private static final String PREFIX = TYPE + DELIMITER;

        public PolicyPermissionExpression(String expressionOrPolicyCode) {
            super(expressionOrPolicyCode);
            setType(TYPE);
        }

        public String getPolicyCode() {
            return getValue();
        }

        @Override
        public boolean contains(PermissionExpression permission) {
            return false; //expands to SimplePermissionExpressions first
        }
    }

    public static class CollectionPermissionExpression extends PermissionExpression {

        private List<PermissionExpression> expressions;

        public List<PermissionExpression> getExpressions() {
            return expressions;
        }

        public CollectionPermissionExpression(String expression) {
            super(expression);
            setType(COLLECTION_TYPE);
            this.expressions = new ArrayList<>();
            for (String e : expression.split(COLLECTION_DELIMITER)) {
                PermissionExpression exp = parse(e);
                if (exp != null) this.expressions.add(exp);
            }
        }

        public <E extends PermissionExpression> CollectionPermissionExpression(List<E> expressions) {
            super(ResourceExpression.toString(expressions));
            setType(COLLECTION_TYPE);
            if (expressions != null) {
                this.expressions = expressions.stream().collect(Collectors.toList());
            } else {
                this.expressions = new ArrayList<>();
            }
        }

        public CollectionPermissionExpression distinct() {
            return new CollectionPermissionExpression(expressions.stream().distinct().collect(Collectors.toList()));
        }

        @Override
        public boolean contains(PermissionExpression permission) {
            if (expressions == null || expressions.size() == 0 || permission == null || permission.getValue() == null) {
                return false;
            }
            for (PermissionExpression exp : expressions) {
                if (exp != null && exp.contains(permission)) {
                    return true;
                }
            }
            return false;
        }
    }

}
