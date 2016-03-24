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


    public static class OrgRestrictionExpression extends RestrictionExpression {

        private static final String RESOURCE = "org";
        private static final String PREFIX = RESOURCE + DELIMITER;

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
        private static final String PREFIX = RESOURCE + DELIMITER;

        public RegionRestrictionExpression(String expressionOrRegionId) {
            super(expressionOrRegionId);
            setResource(RESOURCE);
        }

        public String getRegionId() {
            return getValue();
        }
    }

    public static class CollectionRestrictionExpression extends RestrictionExpression {

        private List<RestrictionExpression> expressions;

        public List<RestrictionExpression> getExpressions() {
            return expressions;
        }

        public CollectionRestrictionExpression(String expression) {
            super(expression);
            this.expressions = new ArrayList<>();
            for (String e : expression.split(COLLECTION_DELIMITER)) {
                RestrictionExpression exp = parse(e);
                if (exp != null) this.expressions.add(exp);
            }
        }

        public <E extends RestrictionExpression> CollectionRestrictionExpression(List<E> expressions) {
            super(ResourceExpression.toString(expressions));
            if (expressions != null) {
                this.expressions = expressions.stream().collect(Collectors.toList());
            } else {
                this.expressions = new ArrayList<>();
            }
        }
    }

}
