package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrganizationCategoryEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yan on 5/24/2016.
 */
public interface OrganizationCategoryRepository extends CrudRepository<OrganizationCategoryEntity, String> {

    List<OrganizationCategoryEntity> findByOrgId(String id);

    List<OrganizationCategoryEntity> findByOrgIdAndName(String orgId, String name);

    List<OrganizationCategoryEntity> findAll();

    Long deleteByOrgId(String orgId);
}
