package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.TagEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagRepository extends FindOneAndSaveRepository<TagEntity, String> {

    Page<TagEntity> findByDeletedFalseAndOrgId(String orgId, Pageable pageable);

    TagEntity findByDeletedFalseAndId(String id);

    List<TagEntity> findByDeletedFalseAndNameAndOrgId(String name, String orgId);

    void delete(String id);
}
