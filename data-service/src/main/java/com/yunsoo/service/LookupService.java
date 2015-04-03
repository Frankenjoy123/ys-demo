package com.yunsoo.service;

import com.yunsoo.service.contract.AbstractLookup;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
public interface LookupService<T extends AbstractLookup> {

    public T getById(int id);

    public List<T> getAll(Boolean activeOnly);

    public T save(T lookup);

    public void delete(T lookup);
}
