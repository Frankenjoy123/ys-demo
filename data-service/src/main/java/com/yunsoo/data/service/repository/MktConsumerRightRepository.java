package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktConsumerRightEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Created by  : haitao
 * Created on  : 2016/6/21
 * Descriptions:
 */
public interface MktConsumerRightRepository extends FindOneAndSaveRepository<MktConsumerRightEntity, String> {

    Page<MktConsumerRightEntity> findByOrgId(String orgId, Pageable pageable);

}
