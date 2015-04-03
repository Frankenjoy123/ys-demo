package com.yunsoo.common.data.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public class LookupObject {
    private Integer id;
    private String code;
    private String name;
    private String description;
    private boolean active;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
        return this.name + "(" + this.code + ": " + this.id + ")";
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : this.getId();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj
                || (obj != null
                && this.getId() != null
                && this.getClass().equals(obj.getClass())
                && this.getId().equals(((LookupObject) obj).getId()));
    }


    public static <T extends LookupObject> List<String> changeIdToCode(List<T> lookup, List<Integer> idList) {
        return fromIdList(lookup, idList).stream().map(LookupObject::getCode).collect(Collectors.toList());
    }

    public static <T extends LookupObject> List<Integer> changeCodeToId(List<T> lookup, List<String> codeList) {
        return fromCodeList(lookup, codeList).stream().map(LookupObject::getId).collect(Collectors.toList());
    }

    public static <T extends LookupObject> List<T> fromIdList(List<T> lookup, List<Integer> list) {
        if (lookup == null) {
            throw new IllegalArgumentException("lookup is null");
        }
        if (list == null) {
            throw new IllegalArgumentException("list is null");
        }
        return list.stream().map(id -> {
            if (id != null) {
                for (T i : lookup) {
                    if (id.equals(i.getId())) {
                        return i;
                    }
                }
            }
            throw new IllegalArgumentException("invalid id: " + id);
        }).collect(Collectors.toList());
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
