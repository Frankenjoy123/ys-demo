package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.PermissionResourceEntity;
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
