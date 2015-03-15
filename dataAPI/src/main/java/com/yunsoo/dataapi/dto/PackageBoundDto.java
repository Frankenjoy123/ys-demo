package com.yunsoo.dataapi.dto;

import com.yunsoo.service.contract.PackageBoundContract;

import java.util.List;

/**
 * Created by Hope on 2015/2/7.
 */
public class PackageBoundDto {
    private long operator;
    private String packageKey;
    private List<String> keys;

    public PackageBoundDto() {
    }

    public PackageBoundDto(long operator, String packageKey, List<String> keys) {
        this.operator = operator;
        this.packageKey = packageKey;
        this.keys = keys;
    }

    public String getPackageKey() {
        return packageKey;
    }

    public void setPackageKey(String packageKey) {
        this.packageKey = packageKey;
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

    public PackageBoundContract toServiceContract() {
        PackageBoundContract contract = new PackageBoundContract();
        contract.setKeys(this.keys);
        contract.setOperator(this.operator);
        contract.setPackageKey(this.packageKey);
        return contract;
    }

}
