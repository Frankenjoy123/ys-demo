package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.DomainDirectoryEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-04-12
 * Descriptions:
 */
public interface DomainDirectoryRepository extends FindOneAndSaveRepository<DomainDirectoryEntity, String> {

    List<DomainDirectoryEntity> findAll();

    void delete(String name);
}
