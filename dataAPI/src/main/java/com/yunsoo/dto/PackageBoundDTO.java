package com.yunsoo.dto;

import java.util.List;

/**
 * Created by Hope on 2015/2/7.
 */
public class PackageBoundDTO {
    private long operator;
    private final String packageKey;
    private List<String> keys;
    

    public PackageBoundDTO(long operator, String packageKey, List<String> keys) {
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
