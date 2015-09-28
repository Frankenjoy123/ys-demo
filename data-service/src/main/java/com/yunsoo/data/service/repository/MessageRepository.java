package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MessageEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2015/8/11
 * Descriptions:
 */
public interface MessageRepository extends FindOneAndSaveRepository<MessageEntity, String> {

    Page<MessageEntity> findByOrgId(String orgId, Pageable pageable);

    Long countByOrgId(String orgId);

    Long countByOrgIdAndTypeCodeIn(String orgId, List<String> typeCodeIn);

    Long countByOrgIdAndStatusCodeIn(String orgId, List<String> statusCodeIn);

    Long countByOrgIdAndTypeCodeInAndStatusCodeIn(String orgId, List<String> typeCodeIn, List<String> statusCodeIn);

    void delete(String id);
}
