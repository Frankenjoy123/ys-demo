package com.yunsoo.data.service.service.contract;

import com.yunsoo.data.service.entity.AbstractLookupEntity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
public abstract class AbstractLookup {

    private Integer id;
    private String code;
    private String name;
    private String description;
    private Boolean active;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    protected static <E extends AbstractLookupEntity, T extends AbstractLookup> E toEntity(Class<E> entityType, T lookup) {
        if (lookup == null) {
            return null;
        }
        try {
            E entity = entityType.newInstance();
            entity.setId(lookup.getId());
            entity.setCode(lookup.getCode());
            entity.setName(lookup.getName());
            entity.setDescription(lookup.getDescription());
            entity.setActive(lookup.isActive());
            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    protected static <E extends AbstractLookupEntity, T extends AbstractLookup> List<E> toEntities(Class<E> entityType, List<T> lookupList) {
        if (lookupList == null) {
            return null;
        }
        return lookupList.stream()
                .map(i -> toEntity(entityType, i))
                .collect(Collectors.toList());
    }

    protected static <T extends AbstractLookup, E extends AbstractLookupEntity> T fromEntity(Class<T> lookupType, E entity) {
        if (entity == null) {
            return null;
        }
        try {
            T lookup = lookupType.newInstance();
            lookup.setId(entity.getId());
            lookup.setCode(entity.getCode());
            lookup.setName(entity.getName());
            lookup.setDescription(entity.getDescription());
            lookup.setActive(entity.isActive());
            return lookup;
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    protected static <T extends AbstractLookup, E extends AbstractLookupEntity> List<T> fromEntities(Class<T> lookupType, Iterable<E> entities) {
        if (entities == null) {
            return null;
        }
        return StreamSupport.stream(entities.spliterator(), false)
                .map(i -> fromEntity(lookupType, i))
                .collect(Collectors.toList());
    }
}

