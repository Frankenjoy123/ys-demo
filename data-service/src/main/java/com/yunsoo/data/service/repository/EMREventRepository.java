package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.EMREventEntity;
import com.yunsoo.data.service.entity.EMRUserEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EMREventRepository extends FindOneAndSaveRepository<EMREventEntity, String>, CustomEMREventRepository {
}
