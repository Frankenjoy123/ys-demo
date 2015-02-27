package com.yunsoo.api.dto;

/**
 * Created by:   Lijian
 * Created on:   2015/2/27
 * Descriptions:
 */
public abstract class StatusBase {
    private int id;
    private String code;
    private String description;
    private boolean active;

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

    public void setDescription(String description) {
        description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return this.code;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj
                || (obj instanceof StatusBase
                && this.getId() == ((StatusBase) obj).getId());
    }
}
