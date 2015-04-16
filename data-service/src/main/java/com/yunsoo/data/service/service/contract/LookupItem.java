package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.AbstractLookupEntity;
import org.springframework.util.Assert;

/**
 * Created by:   Lijian
 * Created on:   2015/4/16
 * Descriptions:
 */
public class LookupItem {

    private String code;
    private String name;
    private String description;
    private Boolean active;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LookupItem() {
    }

    public LookupItem(AbstractLookupEntity entity) {
        Assert.notNull(entity, "entity must not be null");

        setCode(entity.getCode());
        setName(entity.getName());
        setDescription(entity.getDescription());
        setActive(entity.isActive());
    }

    @Override
    public String toString() {
        return "LookupItem{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }

    public <E extends AbstractLookupEntity> E toEntity(Class<E> entityType) {
        try {
            E entity = entityType.newInstance();
            entity.setCode(getCode());
            entity.setName(getName());
            entity.setDescription(getDescription());
            entity.setActive(isActive());
            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("create instance failed for type: " + entityType.getName());
        }
    }
}
