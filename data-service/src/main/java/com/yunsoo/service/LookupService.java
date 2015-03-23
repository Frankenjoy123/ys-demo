package com.yunsoo.service;

import com.yunsoo.service.contract.lookup.AbstractLookup;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
public interface LookupService {

    public <T extends AbstractLookup> T getById(Class<T> lookupType, int id);

    public <T extends AbstractLookup> List<T> getAll(Class<T> lookupType, Boolean active);

    public <T extends AbstractLookup> void save(T model);

    public <T extends AbstractLookup> void update(T model);

    public <T extends AbstractLookup> void delete(T model);
}
