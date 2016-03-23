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

    protected static final String SP = "/";


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
        if (expressionOrValue.contains(SP)) {
            String[] tempArray = expressionOrValue.split(SP, 2);
            this.resource = tempArray[0];
            this.value = tempArray[1];
        } else {
            this.resource = null;
            this.value = expressionOrValue;
        }
        this.expression = expressionOrValue;
    }

    protected void setResource(String resource) {
        if (this.resource != null && this.resource.length() > 0 && !this.resource.equals(resource)) {
            throw new IllegalArgumentException("resource not match");
        }
        if (resource == null) {
            this.expression = value;
        } else if (this.resource == null || this.resource.length() == 0) {
            this.expression = String.format("%s%s%s", resource, SP, this.value);
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

}
