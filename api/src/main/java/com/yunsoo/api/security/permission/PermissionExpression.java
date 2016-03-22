package com.yunsoo.api.security.permission;

/**
 * Created by:   Lijian
 * Created on:   2016-03-21
 * Descriptions:
 */
public class PermissionExpression {

    private String expression;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public static class SimplePermissionExpression extends PermissionExpression {

        private String resourceCode;

        private String actionCode;

    }

    public static class PolicyPermissionExpression extends PermissionExpression {


    }

}
