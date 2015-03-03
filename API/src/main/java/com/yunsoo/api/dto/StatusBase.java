package com.yunsoo.api.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public abstract class StatusBase<T extends StatusBase> implements Comparable<T> {
    private int id;
    private String code;
    private String description;
    private boolean active;

    public StatusBase() {
    }

    ;

    public StatusBase(int id, String code) {
        this.id = id;
        this.code = code;
        this.description = null;
        this.active = true;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return this.code + "(" + this.id + ")";
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj
                || (this.getClass().isInstance(obj) && this.getId() == ((StatusBase) obj).getId());
    }

    @Override
    public int compareTo(T obj) {
        return Integer.compare(this.getId(), obj.getId());
    }
}
