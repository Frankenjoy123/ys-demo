package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrgAgencyEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2015/9/2
 * Descriptions:
 */
public interface OrgAgencyRepository extends FindOneAndSaveRepository<OrgAgencyEntity, String> {

    Page<OrgAgencyEntity> findByOrgIdAndStatusCodeIn(String orgId, List<String> statusCodes, Pageable pageable);

}
