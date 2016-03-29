package com.yunsoo.api.security.permission.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions: key characters: [/,*]
 */
public abstract class RestrictionExpression extends ResourceExpression {

    public RestrictionExpression(String expressionOrValue) {
        super(expressionOrValue);
    }

    public abstract boolean contains(RestrictionExpression restriction);

    public static RestrictionExpression parse(String expression) {
        if (expression == null) {
            return null;
        } else if (expression.contains(COLLECTION_DELIMITER)) {
            return new CollectionRestrictionExpression(expression);
        } else if (expression.startsWith(OrgRestrictionExpression.PREFIX)) {
            return new OrgRestrictionExpression(expression);
        } else if (expression.startsWith(RegionRestrictionExpression.PREFIX)) {
            return new RegionRestrictionExpression(expression);
        }
        return null;
    }

    public static <E extends RestrictionExpression> RestrictionExpression collect(List<E> expressions) {
        return expressions == null || expressions.size() == 0 ? null
                : expressions.size() == 1 ? expressions.get(0)
                : new CollectionRestrictionExpression(expressions);
    }


    public static class OrgRestrictionExpression extends RestrictionExpression {

        private static final String RESOURCE = "org";
        private static final String PREFIX = RESOURCE + DELIMITER;

        public static final OrgRestrictionExpression CURRENT = new OrgRestrictionExpression("current");

        public OrgRestrictionExpression(String expressionOrOrgId) {
            super(expressionOrOrgId);
            setResource(RESOURCE);
        }

        public String getOrgId() {
            return getValue();
        }

        @Override
        public boolean contains(RestrictionExpression restriction) {
            String org = getValue();
            if (org == null || restriction == null || !(restriction instanceof OrgRestrictionExpression) || restriction.getValue() == null) {
                return false;
            }
            return org.equals("*") || org.equals(restriction.getValue());
        }
    }

    public static class RegionRestrictionExpression extends RestrictionExpression {

        private static final String RESOURCE = "region";
        private static final String PREFIX = RESOURCE + DELIMITER;

        public RegionRestrictionExpression(String expressionOrRegionId) {
            super(expressionOrRegionId);
            setResource(RESOURCE);
        }

        public String getRegionId() {
            return getValue();
        }

        @Override
        public boolean contains(RestrictionExpression restriction) {
            return false; //expands to OrgRestrictionExpressions first
        }
    }

    public static class CollectionRestrictionExpression extends RestrictionExpression {

        private List<RestrictionExpression> expressions;

        public List<RestrictionExpression> getExpressions() {
            return expressions;
        }

        public CollectionRestrictionExpression(String expression) {
            super(expression);
            setResource(COLLECTION_RESOURCE);
            this.expressions = new ArrayList<>();
            for (String e : expression.split(COLLECTION_DELIMITER)) {
                RestrictionExpression exp = parse(e);
                if (exp != null) this.expressions.add(exp);
            }
        }

        public <E extends RestrictionExpression> CollectionRestrictionExpression(List<E> expressions) {
            super(ResourceExpression.toString(expressions));
            setResource(COLLECTION_RESOURCE);
            if (expressions != null) {
                this.expressions = expressions.stream().collect(Collectors.toList());
            } else {
                this.expressions = new ArrayList<>();
            }
        }

        @Override
        public boolean contains(RestrictionExpression restriction) {
            if (expressions == null || expressions.size() == 0 || restriction == null || restriction.getValue() == null) {
                return false;
            }
            for (RestrictionExpression exp : expressions) {
                if (exp != null && exp.contains(restriction)) {
                    return true;
                }
            }
            return false;
        }
    }

}
