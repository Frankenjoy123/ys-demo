package com.yunsoo.service.contract;

import java.util.List;

/**
 * Created by Hope on 2015/3/7.
 */
public class PackageBoundContract {
    private List<String> keys;
    private Long operator;
    private String packageKey;

    public PackageBoundContract() {
    }

    public PackageBoundContract(String packageKey, List<String> keys, long operator) {
        this.packageKey = packageKey;
        this.keys = keys;
        this.operator = operator;
    }

    public String getPackageKey() {
        return packageKey;
    }

    public List<String> getKeys() {
        return keys;
    }

    public long getOperator() {
        return operator;
    }

    public void setPackageKey(String packageKey) {
        this.packageKey = packageKey;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
