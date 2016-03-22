package com.yunsoo.api.security.permission.expression;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions:
 */
public abstract class RestrictionExpression extends ResourceExpression {

    public RestrictionExpression(String expressionOrValue) {
        super(expressionOrValue);
    }

    public static RestrictionExpression newInstance(String expression) {
        if (expression == null) {
            return null;
        } else if (expression.startsWith(OrgRestrictionExpression.PREFIX)) {
            return new OrgRestrictionExpression(expression);
        } else if (expression.startsWith(RegionRestrictionExpression.PREFIX)) {
            return new RegionRestrictionExpression(expression);
        }
        return null;
    }

    public static class OrgRestrictionExpression extends RestrictionExpression {

        private static final String RESOURCE = "org";
        private static final String PREFIX = RESOURCE + "/";

        public OrgRestrictionExpression(String expressionOrOrgId) {
            super(expressionOrOrgId);
            setResource(RESOURCE);
        }

        public String getOrgId() {
            return getValue();
        }

    }

    public static class RegionRestrictionExpression extends RestrictionExpression {

        private static final String RESOURCE = "region";
        private static final String PREFIX = RESOURCE + "/";

        public RegionRestrictionExpression(String expressionOrRegionId) {
            super(expressionOrRegionId);
            setResource(RESOURCE);
        }

        public String getRegionId() {
            return getValue();
        }
    }

}
