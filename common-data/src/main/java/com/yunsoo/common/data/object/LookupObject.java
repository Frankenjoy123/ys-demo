package com.yunsoo.common.data.object;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class LookupObject {

    private String code;
    private String name;
    private String description;
    private boolean active;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.code + ")";
    }

    @Override
    public int hashCode() {
        return code == null ? 0 : this.getCode().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj
                || (obj != null
                && this.getCode() != null
                && this.getClass().equals(obj.getClass())
                && this.getCode().equals(((LookupObject) obj).getCode()));
    }

    public static <T extends LookupObject> List<T> fromCodeList(List<T> lookup, List<String> list) {
        if (lookup == null) {
            throw new IllegalArgumentException("lookup is null");
        }
        if (list == null) {
            throw new IllegalArgumentException("list is null");
        }
        return list.stream().map(code -> {
            if (code != null) {
                for (T i : lookup) {
                    if (code.equals(i.getCode())) {
                        return i;
                    }
                }
            }
            throw new IllegalArgumentException("invalid code: " + code);
        }).collect(Collectors.toList());
    }
}
