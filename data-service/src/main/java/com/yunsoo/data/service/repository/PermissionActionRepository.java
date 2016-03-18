package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.PermissionActionEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-16
 * Descriptions:
 */
public interface PermissionActionRepository extends Repository<PermissionActionEntity, String> {

    List<PermissionActionEntity> findAll();

}
