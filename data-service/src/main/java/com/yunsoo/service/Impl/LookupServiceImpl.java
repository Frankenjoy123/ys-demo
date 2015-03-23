package com.yunsoo.service.Impl;

import com.yunsoo.service.LookupService;
import com.yunsoo.service.contract.lookup.AbstractLookup;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
@Service("lookupService")
public class LookupServiceImpl implements LookupService {

    @Override
    public <T extends AbstractLookup> T getById(Class<T> lookupType, int id) {
        return null;
    }

    @Override
    public <T extends AbstractLookup> List<T> getAll(Class<T> lookupType, Boolean active) {
        return null;
    }

    @Override
    public <T extends AbstractLookup> void save(T model) {

    }

    @Override
    public <T extends AbstractLookup> void update(T model) {

    }

    @Override
    public <T extends AbstractLookup> void delete(T model) {

    }


}
