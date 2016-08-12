package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.AttachmentEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by:   yan
 * Created on:   3/22/2016
 * Descriptions:
 */
public interface AttachmentRepository extends FindOneAndSaveRepository<AttachmentEntity, String> {

    List<AttachmentEntity> findByIdIn(List<String> Ids);
}
