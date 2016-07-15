package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
public interface OrganizationRepository extends Repository<OrganizationEntity, String> {

    OrganizationEntity findOne(String id);

    List<OrganizationEntity> findByName(String name);

    Page<OrganizationEntity> findAll(Pageable pageable);

    Page<OrganizationEntity> findByIdIn(Iterable<String> Ids, Pageable pageable);

    OrganizationEntity save(OrganizationEntity entity);
}
