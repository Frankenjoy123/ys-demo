package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.entity.AbstractLookupEntity;
import com.yunsoo.data.service.repository.basic.LookupRepository;
import com.yunsoo.data.service.service.LookupService;
import com.yunsoo.data.service.service.LookupType;
import com.yunsoo.data.service.service.contract.LookupItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by:   Lijian
 * Created on:   2015/3/24
 * Descriptions:
 */
@Service("lookupService")
public class LookupServiceImpl implements LookupService {

    @Autowired
    private List<LookupRepository> repositories;

    @Override
    public LookupItem getByCode(LookupType lookupType, String code) {
        Assert.notNull(lookupType, "lookupType must not be null");
        Assert.notNull(code, "code must not be null");

        LookupRepository repo = findRepositoryFor(lookupType.getRepositoryType());
        AbstractLookupEntity entity = repo.findOne(code);
        return entity == null ? null : new LookupItem(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LookupItem> getAll(LookupType lookupType) {
        Assert.notNull(lookupType, "lookupType must not be null");

        LookupRepository repo = findRepositoryFor(lookupType.getRepositoryType());
        Iterable<AbstractLookupEntity> entities = repo.findAll();
        return StreamSupport.stream(entities.spliterator(), false).map(LookupItem::new).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LookupItem> getAllActive(LookupType lookupType) {
        Assert.notNull(lookupType, "lookupType must not be null");

        LookupRepository repo = findRepositoryFor(lookupType.getRepositoryType());
        Iterable<AbstractLookupEntity> entities = repo.findByActive(true);
        return StreamSupport.stream(entities.spliterator(), false).map(LookupItem::new).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public LookupItem save(LookupType lookupType, LookupItem lookupItem) {
        Assert.notNull(lookupType, "lookupType must not be null");
        Assert.notNull(lookupItem, "lookupItem must not be null");

        LookupRepository repo = findRepositoryFor(lookupType.getRepositoryType());
        AbstractLookupEntity entity = lookupItem.toEntity(lookupType.getEntityType());
        entity = repo.save(entity); //TODO:issue, saving is not successfully
        return new LookupItem(entity);
    }

    private LookupRepository findRepositoryFor(Class<? extends LookupRepository> clazz) {
        for (LookupRepository repository : repositories) {
            if (clazz.isInstance(repository)) {
                return repository;
            }
        }
        throw new RuntimeException("lookup repository not found for " + clazz.getName());
    }
}
