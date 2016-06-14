package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.TagEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagRepository extends FindOneAndSaveRepository<TagEntity, String> {

    Page<TagEntity> findByDeletedFalseAndOrgId(String orgId, Pageable pageable);

    TagEntity findByDeletedFalseAndId(String id);

    void delete(String id);
}
