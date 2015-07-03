package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
public interface OrganizationRepository extends CrudRepository<OrganizationEntity, String> {

    OrganizationEntity findByName(String name);

    Page<OrganizationEntity> findAll(Pageable pageable);
}
