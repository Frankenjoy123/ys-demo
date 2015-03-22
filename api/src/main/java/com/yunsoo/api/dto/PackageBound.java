package com.yunsoo.api.dto;

import java.util.List;

/**
 * Created by Hope on 2015/3/8.
 */
public class PackageBound {
    private long operator;
    private final String packageKey;
    private List<String> keys;


    public PackageBound(String packageKey, List<String> keys, long operator) {
        this.operator = operator;
        this.packageKey = packageKey;
        this.keys = keys;
    }

    public String getPackageKey() {
        return packageKey;
    }


    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public long getOperator() {
        return operator;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }

}