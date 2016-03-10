package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
public interface OrganizationRepository extends CrudRepository<OrganizationEntity, String> {

    List<OrganizationEntity> findByName(String name);

    Page<OrganizationEntity> findAll(Pageable pageable);

    List<OrganizationEntity> findByIdIn(List<String> Ids);
}
