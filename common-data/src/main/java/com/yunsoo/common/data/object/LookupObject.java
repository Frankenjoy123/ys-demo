package com.yunsoo.common.data.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class LookupObject {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("active")
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

    public static <T extends LookupObject> T fromCode(List<T> lookup, String code) {
        if (lookup == null) {
            throw new IllegalArgumentException("lookup is null");
        }
        if (code == null) {
            return null;
        }
        for (T item : lookup) {
            if (code.equals(item.getCode())) {
                return item;
            }
        }
        return null;
    }

    public static <T extends LookupObject> List<T> fromCodeList(List<T> lookup, List<String> codeList) {
        if (lookup == null) {
            throw new IllegalArgumentException("lookup is null");
        }
        if (codeList == null) {
            throw new IllegalArgumentException("list is null");
        }
        return codeList.stream().map(code -> {
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

    public static <T extends LookupObject> List<String> toCodeList(List<T> lookup) {
        if (lookup == null) {
            throw new IllegalArgumentException("lookup is null");
        }
        return lookup.stream().map(LookupObject::getCode).collect(Collectors.toList());
    }
}
