package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.AttachmentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yan on 3/22/2016.
 */
public interface AttachmentRepository extends CrudRepository<AttachmentEntity, String> {

    public List<AttachmentEntity> findByIdIn(List<String> Ids);
}
