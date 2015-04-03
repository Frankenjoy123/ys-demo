package com.yunsoo.repository;

import com.yunsoo.entity.AbstractLookupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
@NoRepositoryBean
public interface LookupRepository<E extends AbstractLookupEntity> extends CrudRepository<E, Integer> {
    Iterable<E> findByActive(Boolean active);
}
