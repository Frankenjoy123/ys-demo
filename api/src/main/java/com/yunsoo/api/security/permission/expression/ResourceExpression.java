package com.yunsoo.api.security.permission.expression;


/**
 * Created by:   Lijian
 * Created on:   2016-03-22
 * Descriptions:
 */
public abstract class ResourceExpression implements Comparable {

    private String resource;

    private String value;

    private String expression;


    public String getResource() {
        return resource;
    }

    protected void setResource(String resource) {
        this.resource = resource;
    }

    public String getValue() {
        return value;
    }

    public String getExpression() {
        return expression;
    }


    public ResourceExpression(String expressionOrValue) {
        if (expressionOrValue.contains("/")) {
            String[] tempArray = expressionOrValue.split("/", 2);
            this.resource = tempArray[0];
            this.value = tempArray[1];
        } else {
            this.value = expressionOrValue;
        }
        this.expression = expressionOrValue;
    }

    public ResourceExpression(String resource, String value) {
        this.resource = resource;
        this.value = value;
        this.expression = String.format("%s/%s", resource, value);
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

}
