package com.yunsoo.service.Impl;

import com.yunsoo.service.LookupService;
import com.yunsoo.service.contract.AbstractLookup;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/24
 * Descriptions: To be deleted
 */
public class LookupServiceImpl<T extends AbstractLookup> implements LookupService<T> {

    @Override
    public T getById(int id) {
        return null;
    }

    @Override
    public List<T> getAll(Boolean activeOnly) {
        return null;
    }

    @Override
    public T save(T model) {
        return null;
    }

    @Override
    public void delete(T model) {

    }
}
