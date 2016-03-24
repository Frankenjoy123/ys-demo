package com.yunsoo.api.security.permission.expression;


import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions: key characters: [/,]
 */
public abstract class ResourceExpression implements Comparable {

    private String resource;

    private String value;

    private String expression;

    protected static final String DELIMITER = "/";
    protected static final String COLLECTION_DELIMITER = ",";
    protected static final String COLLECTION_RESOURCE = "collection";


    public String getResource() {
        return resource;
    }

    public String getValue() {
        return value;
    }

    public String getExpression() {
        return expression;
    }

    protected ResourceExpression(String expressionOrValue) {
        if (expressionOrValue.contains(COLLECTION_DELIMITER)) {
            this.resource = COLLECTION_RESOURCE;
            this.value = expressionOrValue;
        } else if (expressionOrValue.contains(DELIMITER)) {
            String[] tempArray = expressionOrValue.split(DELIMITER, 2);
            this.resource = tempArray[0];
            this.value = tempArray[1];
        } else {
            this.resource = null;
            this.value = expressionOrValue;
        }
        this.expression = expressionOrValue;
    }

    protected void setResource(String resource) {
        if (this.resource != null && this.resource.length() > 0 && !this.resource.equals(resource) && !resource.equals(COLLECTION_RESOURCE)) {
            throw new IllegalArgumentException("resource not match");
        }
        if (resource == null) {
            this.expression = this.value;
        } else if (COLLECTION_RESOURCE.equals(resource)) {
            this.value = this.expression;
        } else if (this.resource == null || this.resource.length() == 0) {
            this.expression = String.format("%s%s%s", resource, DELIMITER, this.value);
        }
        this.resource = resource;
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

}
