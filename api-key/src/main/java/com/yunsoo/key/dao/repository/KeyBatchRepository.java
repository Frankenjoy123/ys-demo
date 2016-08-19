package com.yunsoo.key.dao.repository;

import com.yunsoo.key.dao.entity.KeyBatchEntity;
import org.springframework.data.repository.Repository;

/**
 * Created by:   Lijian
 * Created on:   2016-08-18
 * Descriptions:
 */
public interface KeyBatchRepository extends Repository<KeyBatchEntity, String> {

    KeyBatchEntity findOne(String id);

    KeyBatchEntity save(KeyBatchEntity entity);

}
