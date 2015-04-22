package com.yunsoo.data.service.repository.basic;

import com.yunsoo.data.service.entity.AbstractLookupEntity;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
@NoRepositoryBean
public interface LookupRepository<E extends AbstractLookupEntity> extends Repository<E, String> {

    E findOne(String code);

    Iterable<E> findAll();

    Iterable<E> findByActive(Boolean active);

    E save(E entity);

}
