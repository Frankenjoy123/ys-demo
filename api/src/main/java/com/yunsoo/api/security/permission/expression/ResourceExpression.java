package com.yunsoo.api.security.permission.expression;


import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions: key characters: [/,]
 */
public abstract class ResourceExpression implements Comparable {

    private String type;

    private String value;

    private String expression;

    protected static final String DELIMITER = "/";
    protected static final String COLLECTION_DELIMITER = ",";
    protected static final String COLLECTION_TYPE = "collection";


    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getExpression() {
        return expression;
    }

    protected ResourceExpression(String expressionOrValue) {
        if (expressionOrValue.contains(COLLECTION_DELIMITER)) {
            this.type = COLLECTION_TYPE;
            this.value = expressionOrValue;
        } else if (expressionOrValue.contains(DELIMITER)) {
            String[] tempArray = expressionOrValue.split(DELIMITER, 2);
            this.type = tempArray[0];
            this.value = tempArray[1];
        } else {
            this.type = null;
            this.value = expressionOrValue;
        }
        this.expression = expressionOrValue;
    }

    protected void setType(String type) {
        if (this.type != null && this.type.length() > 0 && !this.type.equals(type) && !COLLECTION_TYPE.equals(type)) {
            throw new IllegalArgumentException("type not match");
        }
        if (type == null) {
            this.expression = this.value;
        } else if (COLLECTION_TYPE.equals(type)) {
            this.value = this.expression;
        } else if (this.type == null || this.type.length() == 0) {
            this.expression = String.format("%s%s%s", type, DELIMITER, this.value);
        }
        this.type = type;
    }

    @Override
    public String toString() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceExpression that = (ResourceExpression) o;

        return expression != null ? expression.equals(that.expression) : that.expression == null;

    }

    @Override
    public int hashCode() {
        return expression != null ? expression.hashCode() : 0;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return -1;
        if (getClass() != o.getClass()) throw new ClassCastException();

        ResourceExpression that = (ResourceExpression) o;

        if (this.equals(that)) return 0;
        if (expression == null) return 1;
        if (that.getExpression() == null) return -1;

        return expression.compareTo(that.expression);
    }

    public static <T extends ResourceExpression> String toString(List<T> resourceExpressions) {
        if (resourceExpressions == null) {
            return "";
        } else if (resourceExpressions.size() == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder(resourceExpressions.get(0).toString());
            for (int i = 1; i < resourceExpressions.size(); i++) {
                sb.append(",").append(resourceExpressions.get(i).toString());
            }
            return sb.toString();
        }
    }

    public static <T extends ResourceExpression> boolean equals(T e1, T e2) {
        return e1 == e2 || e1 != null && e1.equals(e2);
    }

}
