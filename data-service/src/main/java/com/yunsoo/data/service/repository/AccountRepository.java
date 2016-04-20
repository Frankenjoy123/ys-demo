package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.AccountEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/17
 * Descriptions:
 */
public interface AccountRepository extends FindOneAndSaveRepository<AccountEntity, String> {

    Page<AccountEntity> findByOrgIdOrderByCreatedDateTimeDesc(String orgId, Pageable pageable);

    List<AccountEntity> findByOrgIdAndIdentifier(String orgId, String identifier);


    Long count();

    Long countByOrgId(String orgId);

    Long countByStatusCodeIn(List<String> statusCodeIn);

    Long countByOrgIdAndStatusCodeIn(String orgId, List<String> statusCodeIn);
}
