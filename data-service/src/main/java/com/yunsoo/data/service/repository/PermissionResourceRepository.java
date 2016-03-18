package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.PermissionResourceEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-16
 * Descriptions:
 */
public interface PermissionResourceRepository extends Repository<PermissionResourceEntity, String> {

    List<PermissionResourceEntity> findAll();

}
